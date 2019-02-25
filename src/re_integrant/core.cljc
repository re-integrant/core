(ns re-integrant.core
  (:require [re-frame.core :as re-frame]))

(defn doall-multi [multi]
  (some->> multi methods (map #(-> % key multi)) doall))

(defn init-module [config]
  (let [{:keys [reg-sub reg-event reg-fx reg-cofx init-event]} config]
    (doall (map doall-multi [reg-sub reg-event reg-fx reg-cofx]))
    (when init-event (re-frame/dispatch-sync init-event))
    config))

(defn halt-module [module]
  (let [{:keys [reg-sub reg-event reg-fx reg-cofx halt-event]} module]
    (when halt-event (re-frame/dispatch-sync halt-event))
    (some->> reg-event methods (map #(-> % key re-frame/clear-event)) doall)
    (some->> reg-fx methods (map #(-> % key re-frame/clear-fx)) doall)
    (some->> reg-cofx methods (map #(-> % key re-frame/clear-cofx)) doall)
    (some->> reg-sub methods (map #(-> % key re-frame/clear-sub)) doall)))

(defmacro defsub [multi k & body]
  `(defmethod ~multi ~k [k#]
     (re-frame.core/reg-sub
      k# ~@body)))

(defmacro defsub-raw [multi k & body]
  `(defmethod ~multi ~k [k#]
     (re-frame.core/reg-sub-raw
      k# ~@body)))

(defmacro defevent [multi k & body]
  `(defmethod ~multi ~k [k#]
     (re-frame.core/reg-event-db
      k# ~@body)))

(defmacro defevent-fx [multi k & body]
  `(defmethod ~multi ~k [k#]
     (re-frame.core/reg-event-fx
      k# ~@body)))

(defmacro defevent-ctx [multi k & body]
  `(defmethod ~multi ~k [k#]
     (re-frame.core/reg-event-ctx
      k# ~@body)))

(defmacro defcofx [multi k & body]
  `(defmethod ~multi ~k [k#]
     (re-frame.core/reg-cofx
      k# ~@body)))

(defmacro deffx [multi k & body]
  `(defmethod ~multi ~k [k#]
     (re-frame.core/reg-fx
      k# ~@body)))
