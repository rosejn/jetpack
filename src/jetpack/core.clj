(ns jetpack.core
  (:require
    [nomad :refer (defconfig)]
    [ring.middleware.reload :as reload]
    [ring.util.response :as resp]
    ring.middleware.cors
    [cemerick.friend :as friend]
    [cemerick.friend [workflows :as workflows]
                     [credentials :as creds]]
    [compojure.core :as compojure]
    [compojure.core :refer (routes defroutes GET ANY PUT POST DELETE)]
    [compojure.route :as route]
    [compojure.handler :refer (site)]
    [hiccup.core :refer (html)]
    [jetpack.users :refer (users)]
    [jetpack.data :as data]
    [jetpack.template :as tpl]))

(defn render-page
  [req page-name]
  (let [page (get (data/pages) (keyword page-name))
        template (get page :template :page)]
    (html (tpl/render-template template page))))

(defroutes site-routes
  (GET "/" [] (render-page nil "index"))
  ;(GET "/data" [] data-websocket)
  (GET "/test" [] (fn [req] "This is a custom route test")))

(defroutes admin-routes*
  (GET "/" [] (fn [req] "ADMIN PAGE")))

(def admin-routes
  (-> #'admin-routes*
      ;(wrap-basic-authentication admin-authenticated?)
      ))

(defroutes page-routes
  (GET "/page/:page" [page] #(render-page % page))
  (route/files "/" {:root "public"})
  (GET "/login" req
    (html (tpl/login-form)))
  (GET "/logout" req
    (friend/logout* (resp/redirect (str (:context req) "/"))))
  (GET "/requires-authentication" req
    (friend/authenticated "Thanks for authenticating!"))
  (GET "/role-user" req
    (friend/authorize #{:jetpack.users/user} "You're a user!"))
  (GET "/role-admin" req
    (friend/authorize #{:jetpack.users/admin} "You're an admin!"))
  (route/not-found "Page not found"))

(defn app-handler
 [config]
 (-> (site
       (friend/authenticate
       (routes
         (ANY "*" [] #'site-routes)
         (ANY "*" [] #'page-routes)
         ()
         (ANY "/admin/*" [] admin-routes))
       {:allow-anon? true
        :login-uri "/login"
        :default-landing-uri "/"
        :unauthorized-handler #(-> (html [:h2 "You do not have sufficient privileges to access " (:uri %)])
                                   resp/response
                                   (resp/status 401))
        :credential-fn #(creds/bcrypt-credential-fn @users %)
        :workflows [(workflows/interactive-form)]}))

     reload/wrap-reload
     ;(wrap-cors :access-control-allow-origin #".+")
     ))

