(ns exposure.config
  "Namespace that holds configuration"
  (:require [environ.core :refer [env]]
            [clojure.edn :as edn]))

(defn ensure-env
  "Reads value from environment variable or
   throws exception if it is not present and no default value supplied
   (ensure-env :port 3000)
   (ensure-env :client-key)"
  [name & rest]
  (let [default (first rest)
        from-env (edn/read-string (env name))
        value (or from-env default)]
    (if value
      value
      (throw (Exception. (str "Unable to start without " name " parameter"))))))

(def config
  "Global config for application"
  {:port (ensure-env :port 3000)})

