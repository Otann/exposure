(ns exposure.instagram-api
  (:require [taoensso.timbre :as log]
            [environ.core :refer [env]]
            [clj-http.client :as http]))

(def oauth-url "https://api.instagram.com/oauth/access_token")

(defn redirect-url [] (str (env :host) "/api/redirected"))

(defn auth-url []
  (str "https://api.instagram.com/oauth/authorize/"
       "?client_id=" (env :instagram-client-id)
       "&redirect_uri=" (redirect-url)
       "&response_type=code"
       "&scope=public_content"))

(defn redirect-data
  "Handles response from Instaram redirect and
   returns authentication response with access_token"
  [{params :params}]
  (if (:error params)
    (let [reason      (:error-reason)
          description (:error-description)]
      (log/error "Authorization error" reason ":" description)
      ;TODO: throw exception here, handle in some middleware
      nil)

    (let [code (:code params)
          params {:client_id (env :instagram-client-id)
                  :client_secret (env :instagram-client-secret)
                  :grant_type "authorization_code"
                  :redirect_uri (redirect-url)
                  :code code}
          {body :body} (http/post oauth-url
                                  {:as :json
                                   :form-params params})]
      (log/debug "Instagram response for token" body)
      body)))

(defn search
  "Wrapper for instagram API that handles search by tag name"
  [token tag]
  (let [search-url (str "https://api.instagram.com/v1/tags/" tag "/media/recent")
        params     {"access_token" token}
        response   (http/get search-url
                             {:as :json
                              :query-params params})]
    {:status 200
     :body (:body response)}))