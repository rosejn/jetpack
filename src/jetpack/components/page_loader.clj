(ns jetpack.components.page-loader
  (:require [com.stuartsierra.component :as component]
            [jetpack.pages :as pages]
            [taoensso.timbre :as log])
  (:use clojure.tools.trace))

(defrecord PageLoader [page-dir]
  component/Lifecycle

  (start [component]
    (log/info "Starting page loader...")
    (if-not (:stop component)
      (let [loader (pages/start-page-loader page-dir)]
        (assoc component :pages* (:pages* loader)
                         :stop (:stop loader)))
      component))

  (stop [component]
    (when-let [stop-fn (:stop component)]
      (log/info "stopping page loader")
      (log/info "figure out how to turn off watcher...")
      (stop-fn))
    (dissoc component :stop :pages*)))

(defn page-loader-component
  [config]
  (map->PageLoader config))
