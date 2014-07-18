(ns jetpack.websocket
  (:require [chord.http-kit :refer [with-channel]]
            [clojure.core.async :refer [<! >! put! close! go]]))

(def clients (atom {}))

(defn data-websocket
  [req]
  (with-channel req con
    (swap! clients assoc con true)
    (println con " connected")
    ;(on-close con (fn [status]
    ;                (swap! clients dissoc con)
    ;                (println con " disconnected. status: " status)))
    ))

;(defn timer-events []
;  (future (loop []
;            (doseq [client @clients]
;              (put! (key client) {:event :timer :data (rand-int 100)}))
;            (Thread/sleep 1000)
;            (recur))))
;
