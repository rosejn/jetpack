(ns jetpack.template
  (:require [hiccup.core :refer (html)]
            [hiccup.page :refer (html5 include-js include-css)]
            [clj-time.core :as t]))

(defmulti render-template
  (fn [tpl content] tpl))

(defn stylesheets []
  (map include-css [
                    "/css/bootstrap.min.css"
                    "/css/jetpack.css"
                    ]))

(defn javascripts
  []
  (list
    [:script {:src "/js/jquery-2.1.1.min.js"}]
    [:script {:src "/js/bootstrap.min.js"}]))

(defn navigation
  []
  [:div.navbar.navbar-inverse.navbar-fixed-top {:role "navigation"}
   [:div.container
    [:div.navbar-header
     [:button.navbar-toggle {:type "button" :data-toggle "collapse"}
      [:span.sr-only "Toggle Navigation"]
      (repeat 3 [:span.icon-bar])]
     [:a.navbar-brand {:href "/"} "JetPack"]]
     [:div.collapse.navbar-collapse
      [:ul.nav.navbar-nav
       [:li.active [:a {:href "/blog"} "Blog"]]
       [:li [:a {:href "/about"} "About"]]]]]])

(defn footer
  [content]
  [:div.sticky-footer
   [:div.container content]])

(defn blog-post
  [topic title body timestamp]
  (let [today (t/now)
        days  (t/in-days (t/interval timestamp today))
        hours (t/in-hours (t/interval timestamp today))
        date-str  (cond
                    (zero? days) (str hours " hours ago")
                    (= 1 days) "yesterday"
                    :defaul (str days " days ago"))]
    [:article.post.type-post.hentry
     [:header.entry-header
      [:h1.entry-title
       [:a {:rel "bookmark", :title title, :href "blog-single.html"} title]]]

     [:footer.entry-meta "posted in"
      [:a {:title (str "View all posts in " topic) :href (str "/blog/topic/" topic)} topic]
      "last updated " [:time {:datetime date-str, :class "entry-date"} date-str]

      (comment [:span {:class "comments-link"}
                [:a {:title title, :href "blog-single.html#comments"} "5 Comments"]])
      ]

     [:div {:class "entry-content"}
      body

      (comment [:p "Responsive web design offers us a way forward, finally allowing us to design for the ebb and flow of things. There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly.\n                                        " [:a {:class "more-link", :href "blog-single.html"} "Continue reading " [:span {:class "meta-nav"} "→"]]])
      ]]))


(defn google-font
  [family & [options]]
  [:link {:type "text/css", :rel "stylesheet",
          :href (str "http://fonts.googleapis.com/css?family=" family options)}])

(defn page
  [title content]
  [:html
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:content "width=device-width, initial-scale=1, maximum-scale=1", :name "viewport"}]
    [:title title]
    (stylesheets)]
   [:body
    (navigation)
    [:div.container.main content]
    (footer "Your footer here...")
    (javascripts)]])

(defn blog-body
  [posts]
  [:div.blog-posts.readable-content
   (map (fn [{:keys [topic title body timestamp]}] (blog-post topic title body timestamp)) posts)])

(defmethod render-template :page
  [_ {:keys [title body]}]
  (println "render-template: " title)
  (page title
        [:div {:class "readable-content row-fluid page"}
         [:article {:class "page hentry"}
          [:header {:class "entry-header"}
           [:h1 {:class "entry-title"}  title]]
          [:div {:class "entry-content"}
           ;[:p {:class "lead"} title]
           body]]]))

(defmethod render-template :recipe
  [_ {:keys [title recipes]}]
  (page title
        [:div {:class "readable-content row-fluid page"}
         (for [recipe recipes]
           [:article {:class "page hentry"}
            [:header {:class "entry-header"}
             [:h1 {:class "entry-title"} (:name recipe)]]
            [:h3 "Ingredients"]
            [:ul
             (for [item (:ingredients recipe)]
               [:li item])]
            [:h3 "Directions"]
            (:directions recipe)])]))

(defn login-form
  []
  (page "Login"
        [:div {:class "row"}
         [:div {:class "columns small-12"}
          [:h3 "Login"]
          [:div {:class "row"}
           [:form {:method "POST" :action "login" :class "columns small-4"}
            [:div "Username" [:input {:type "text" :name "username"}]]
            [:div "Password" [:input {:type "password" :name "password"}]]
            [:div [:input {:type "submit" :class "button" :value "Login"}]]]]]]))

