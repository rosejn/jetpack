(ns jetpack.data
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [me.raynes.fs :as fs]
            [markdown.core :refer (md-to-html-string)]
            [clj-time.coerce :as time]))

(def PAGE-DIR "pages")

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

(defn pages
  []
  (into {}
        (map (fn [e]
               [(keyword (first (string/split (.getName (:file e)) #"\."))) e])
             (load-dir PAGE-DIR))))

