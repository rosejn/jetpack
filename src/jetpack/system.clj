(ns jetpack.system
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre :as log]
            [jetpack.components.http-server :refer [http-server-component]]
            [jetpack.components.router :refer [router-component]]))

(defn jetpack-system
  [{:keys [router http] :as config}]
  (log/info "Starting jetpack system...")
  (component/system-map
    :router (component/using (router-component router) [])
    :server  (component/using (http-server-component http) [:router])))


