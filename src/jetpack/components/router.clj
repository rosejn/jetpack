(ns jetpack.components.router
  (:require [com.stuartsierra.component :as component]
            [jetpack.core :refer [app-handler]]
            [taoensso.timbre :as log]))

(defrecord Router [config]
  component/Lifecycle
  (start [component]
    (if (:handler component)
      component
      (assoc component :handler (app-handler config))))
  (stop [component]
    (dissoc component :handler)))

(defn router-component [config]
  (map->Router {:config config}))

