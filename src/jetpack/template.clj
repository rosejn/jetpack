(ns jetpack.template
  (:require [hiccup.core :refer (html)]
            [hiccup.page :refer (html5 include-js include-css)]
            [jetpack.data :as data]
            [clj-time.core :as t]))

(defmulti render-template
  (fn [tpl content] tpl))

(defn stylesheets
  []
  (conj
    (map include-css
         ["/css/grid.css"
          "/css/style.css"
          "/css/normalize.css"
          "/css/font-awesome.css"
          ;"/js/google-code-prettify/prettify.css"
          "/css/uniform.default.css"
          "/css/main.css"
          "/css/flexslider.css"
          "/js/syntaxhighlighter_3.0.83/styles/shCoreDefault.css"
          "http://fonts.googleapis.com/css?family=Libre+Baskerville:400,700"])
    [:link {:href "/css/print.css", :media "print", :type "text/css", :rel "stylesheet"}]))

(defn javascripts
  []
  (list
    [:script {:src "/js/detectmobilebrowser.js"}]
    [:script {:src "/js/modernizr.js"}]
    [:script {:src "/js/jquery.imagesloaded.min.js"}]
    [:script {:src "/js/jquery.fitvids.js"}]
    ;[:script {:src "/js/google-code-prettify/prettify.js"}]
    [:script {:src "/js/jquery.uniform.min.js"}]
    [:script {:src "/js/MathJax/MathJax.js?config=TeX-AMS-MML_HTMLorMML,lg"}]
    [:script {:src "/js/syntaxhighlighter_3.0.83/scripts/shCore.js"}]
    [:script {:src "/js/syntaxhighlighter_3.0.83/scripts/shBrushJScript.js"}]
    [:script {:src "/js/syntaxhighlighter_3.0.83/scripts/shBrushClojure.js"}]
    [:script {:src "/js/syntaxhighlighter_3.0.83/scripts/shBrushBash.js"}]
    [:script {:src "/js/main.js"}]))

(defn title-banner
  []
  [:hgroup
   [:h1 {:class "site/-title"} " " [:a {:rel "blog", :title "Life is a Graph", :href "/"} "Life is a Graph"] " "]
   (comment  [:h2 {:class "site-description"} ". . . and you are a node"])])

(defn navigation
  []
  [:nav {:role "navigation", :class "main-navigation", :id "site-navigation"}
   [:ul
    [:li {:class "current-menu-item"}
     [:a {:href "/blog"} "Blog"]]
    ;[:li [:a {:href "projects"} "Projects"]]
    ;[:li [:a {:href "projects"} "Books"]]
    ;[:li [:a {:href "projects"} "Food"]]
    [:li [:a {:href "/about"} "About"]]
    (comment  [:li
     [:form {:action "#", :id "search-form", :method "get", :role "search"}
      [:label {:for "search", :class "screen-reader-text"} "Search"]
      [:input {:title "Enter keyword", :id "search", :name "s", :value "", :type "text"}]
      [:input {:value "→", :title "Search it", :id "search-submit", :type "submit"}]]])
    ]])

(defn footer
  []
  [:footer {:role "contentinfo", :class "site-footer wrapper"}
   [:div.row
    [:div#supplementary.row-fluid]]])


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
    [:meta {:content "IE=edge,chrome=1", :http-equiv "X-UA-Compatible"}]
    [:meta {:content "Read - Responsive HTML5 Template", :name "description"}]
    [:meta {:content "read, blog, html5, portfolio", :name "keywords"}]

    [:title "Life is a Graph - " title]

    [:link {:href "images/ico/favicon.ico", :rel "shortcut icon"}]
    [:link {:href "images/ico/logo-144.png", :sizes "144x144", :rel "apple-touch-icon-precomposed"}]
    [:link {:href "images/ico/logo-114.png", :sizes "114x114", :rel "apple-touch-icon-precomposed"}]
    [:link {:href "images/ico/logo-72.png", :sizes "72x72", :rel "apple-touch-icon-precomposed"}]
    [:link {:href "images/ico/logo-57.png", :rel "apple-touch-icon-precomposed"}]

    (google-font "Lora" ":400,700,400italic,700italic")
    (google-font "Rationale")
    (google-font "Coustard")

    ;(stylesheets)

    [:script {:src "/js/jquery-1.8.3.min.js"}]
    [:script "window.jQuery || document.write('<script src=\"/js/jquery-1.8.3.min.js\"><\\/script>')"]]

   [:body
    [:div#page.hfeed.site
     [:header.site-header.wrapper {:role "banner"}
      [:div.row
       (title-banner)
       (navigation)]]

  [:section#main.middle.wrapper
   [:div.row.row-fluid
    [:div#primary.site-content
     [:div#content {:role "main"} content ]]]]

     (footer)]
    ;(javascripts)
    ]])

(defn blog-body
  [posts]
  [:div.blog-posts.readable-content
   (map (fn [{:keys [topic title body timestamp]}] (blog-post topic title body timestamp)) posts)])

(defn about
  []
  (let [content (:about (data/pages))]
    (page "Life is a Graph - About"
          [:div.about-page {:class "readable-content row-fluid page"}
           [:article {:class "page hentry"}
            [:header {:class "entry-header"}
             [:img {:alt "profile-image", :src "images/about_banner.jpg"}]]
            [:div {:class "entry-content"}
             ;[:h1 {:class "entry-title"} "Hello..."]
             [:p {:class "lead"} (:title content)]
             (:body content)]]
           [:h3 "On the Web"]
           [:ul {:class "social"}
            [:li [:a {:href "http://github.com/rosejn", :class "github"} "GitHub"]]
            [:li [:a {:href "http://www.linkedin.com/in/rosejn", :class "linkedin"} "LinkedIn"]]
            [:li [:a {:href "https://twitter.com/rosejn", :class "twitter"} "Twitter"]]
            ;[:li [:a {:href "#", :class "google"} "k"]]
            ]])))

(defmethod render-template :page
  [_ {:keys [title body]}]
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
