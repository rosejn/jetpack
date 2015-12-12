(ns jetpack.pages
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [jetpack.config :refer (CONFIG)]
            [me.raynes.fs :as fs]
            [markdown.core :refer (md-to-html-string)]
            [clj-time.coerce :as time]
            [clojure-watch.core :refer [start-watch]]))

(defmulti render-content
  (fn [ftype content] ftype))

(defmethod render-content :txt
  [_ content]
  [:div content])

(defmethod render-content :html
  [_ content]
  [:div content])

(defmethod render-content :md
  [_ content]
  (md-to-html-string content))

(defmethod render-content :edn
  [_ content]
  content)

(defmethod render-content :default
  [_ content]
  content)

(defn- load-page
  [file]
  (let [[base suffix] (fs/split-ext file)
        suffix        (string/replace-first suffix #"\." "")
        suffix        (or suffix "txt")
        txt           (slurp file)
        [header-str txt]   (string/split txt #"(?m)^-+" 2)
        header        (load-string header-str)
        timestamp     (time/from-long (.lastModified file))
        etype         (keyword suffix)]
    (assoc header
           :file file
           :topic (fs/name (fs/parent file))
           ;:title (render-content etype title)
           :body (render-content etype txt)
           :timestamp timestamp)))

(defn valid-suffix?
  [fname]
  (let [suffixes (map name (keys (dissoc (.getMethodTable render-content) :default)))
        pattern (re-pattern (str (apply str "\\.(" (interpose "|" suffixes)) ")$"))
        match? (re-find pattern fname)]
    (not (nil? match?))))

(defn load-dir
  [dir]
  (let [entry-dir (clojure.java.io/file dir)
        entries   (filter #(valid-suffix? (.getName %)) (filter fs/file? (file-seq entry-dir)))]
    (map load-page entries)))

(defn load-pages
  [path]
  (into {}
        (map (fn [e]
               [(first (string/split (.getName (:file e)) #"\.")) e])
             (load-dir path))))

(defn get-page
  [loader page-name]
  (println "LOADER: ")
  (println loader)
  (println "PAGE-NAME: " page-name)
  (println @(:pages* loader))
  (get @(:pages* loader) page-name))

; TODO:
; * load only a single page that was created or modified, and delete entry
; for a page that was removed, rather than reloading all.
(defn start-page-loader
  [page-dir]
  (let [pages* (atom {})
        stop-watcher (start-watch [{:path page-dir
                                    :event-types [:create :modify :delete]
                                    :options {:recursive true}
                                    :bootstrap (fn [path]
                                                 (println "Watching page directory: " path)
                                                 (reset! pages* (load-pages path)))
                                    :callback (fn [event filename]
                                                (println event filename)
                                                (reset! pages* (load-pages page-dir)))
                                    }])]
    {:pages* pages*
     :stop stop-watcher}))

