(ns exposure.instagram-api
  (:require [taoensso.timbre :as log]
            [environ.core :refer [env]]
            [clj-http.client :as http]))

(def oauth-url "https://api.instagram.com/oauth/access_token")

(defn redirect-url [] (str (env :host) "/api/redirected"))

(defn auth-url []
  (str "https://api.instagram.com/oauth/authorize/?client_id="
       (env :instagram-client-id)
       "&redirect_uri="
       (redirect-url)
       "&response_type=code"))

(defn redirect-data
  "Handles response from Instaram redirect and
   returns authentication response with access_token"
  [{params :params}]
  (if (:error params)
    (let [reason      (:error-reason)
          description (:error-description)]
      (log/error "Authorization error" reason ":" description)
      {:status 502
       :body {:reason reason
              :description description}})

    (let [code (:code params)
          params {:client_id (env :instagram-client-id)
                  :client_secret (env :instagram-client-secret)
                  :grant_type "authorization_code"
                  :redirect_uri (redirect-url)
                  :code code}
          {body :body} (http/post oauth-url {:as :json
                                             :form-params params})]
      (log/debug "Instagram response for token" body)
      body)))
