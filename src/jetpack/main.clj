(ns jetpack.main
  (:gen-class)
  (:require
    [com.stuartsierra.component :as component]
    [jetpack.config :refer [CONFIG]]
    [jetpack.system :refer [jetpack-system]]))


(defn -main [& args]
  (component/start
    (jetpack-system (CONFIG))))
