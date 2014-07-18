(ns jetpack.main
  (:gen-class)
  (:require
    [com.stuartsierra.component :as component]
    [taoensso.timbre :as timbre]
    [clojure.java.io :as io]
    [nomad :refer [defconfig]]
    [jetpack.system :refer [jetpack-system]]))

(timbre/set-config! [:appenders :spit :enabled?] true)
(timbre/set-config! [:shared-appender-config :spit-filename] "log/site.log")

(defconfig CONFIG (io/resource "config.edn"))

(defn -main [& args]
  (let [config (CONFIG)]
    (component/start
      (jetpack-system config))))
