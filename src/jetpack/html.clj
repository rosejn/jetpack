(ns jetpack.html
  (:use net.cgrand.enlive-html)
  (:import java.io.StringReader))

(defn enlive->hiccup
  [el]
  (println el)
  (if-not (string? el)
    (->> (map enlive->hiccup (:content el))
         (concat [(:tag el) (:attrs el)])
         (keep identity)
         vec)
    el))


(defn html->enlive
  [html]
  (first (html-resource (StringReader. html))))


(defn html-str->hiccup
  [html]
  (-> html
      html->enlive
      enlive->hiccup))


(defn html->hiccup
  [path]
  (html-str->hiccup (slurp path)))

