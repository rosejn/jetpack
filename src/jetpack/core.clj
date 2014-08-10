(ns jetpack.core
  (:require
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
    [hiccup.page :refer (html5)]
    [jetpack.users :refer (users)]
    [jetpack.pages :as pages]
    [jetpack.template :as tpl]))

(defn render-page
  [loader page-name]
  (println "render-page: " page-name)
  (let [page (pages/get-page loader page-name)
        template (get page :template :page)]
    (html5 (tpl/render-template template page))))

(defroutes admin-routes
  (GET "/" [] (fn [req] "ADMIN PAGE")))

(defroutes auth-routes
  (GET "/login" req
    (html5 (tpl/login-form)))
  (GET "/logout" req
    (friend/logout* (resp/redirect (str (:context req) "/"))))
  (GET "/requires-authentication" req
    (friend/authenticated "Thanks for authenticating!"))
  (GET "/role-user" req
    (friend/authorize #{:jetpack.users/user} "You're a user!"))
  (GET "/role-admin" req
    (friend/authorize #{:jetpack.users/admin} "You're an admin!")))

(defn page-routes
  [page-loader]
  (routes
    (GET "/:page" [page] (fn [_] (render-page page-loader page)))
    (route/files "/" {:root "public"})
    (route/not-found (html5 (tpl/page "Page Not Found" [:p "Page not found"])))))

(defroutes site-routes
  (GET "/" [] (render-page nil "index"))
  ;(GET "/data" [] data-websocket)
  (GET "/test" [] (fn [req] "This is a custom route test")))

(defn app-handler
 [page-loader]
 (-> (site
       (friend/authenticate
       (routes
         (ANY "*" []        #'auth-routes)
         (ANY "/admin/*" [] #'admin-routes)
         (ANY "*" []        #'site-routes)
         (ANY "*" []        (page-routes page-loader)))
       {:allow-anon? true
        :login-uri "/login"
        :default-landing-uri "/"
        :unauthorized-handler #(-> (html5 [:h2 "You do not have sufficient privileges to access " (:uri %)])
                                   resp/response
                                   (resp/status 401))
        :credential-fn #(creds/bcrypt-credential-fn @users %)
        :workflows [(workflows/interactive-form)]}))

     reload/wrap-reload
     ;(wrap-cors :access-control-allow-origin #".+")
     ))

