(ns exposure.server
  (:require [clojure.tools.logging :as log]
            [exposure.handler :refer [app]]
            [ring.adapter.jetty :refer [run-jetty]]
            [environ.core :refer [env]])
  (:gen-class))

(def required-keys [:host
                    :instagram-client-id
                    :instagram-client-secret])

(def port (Integer/parseInt (env :port "3000")))

(defn require-env! [vars]
  "Checks that all variables with provided names are present in env"
  (let [missing (remove #(env %) vars)]
    (when (seq missing)
      (doseq [var missing] (log/error "Variable" var "was not provided"))
      (System/exit 0))))

(defn -main [& args]
  (require-env! required-keys)
  (run-jetty app {:port port :join? false}))
