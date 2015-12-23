(ns exposure.views.pages
  (:require [re-frame.core :refer [subscribe dispatch]]
            [reagent.ratom :refer-macros [reaction]]

            [exposure.routes :refer [url-for]]
            [exposure.views.components :as c]
            [exposure.views.google-maps :as google-maps]))

(defn welcome []
  [:div.ui.vertical.masthead.center.aligned.segment
   {:style {:min-height "500px"}}
   [:div.ui.test.container
    [:h1.header {:style {:margin-top "2em"
                         :font-size "3em"}}
     "Welcome to Exposure"]
    [:h3 "Web application to browse and analyse Instagram data"]
    [:a.ui.huge.primary.button {:href "/api/auth"}
     "Start browsing"
     [:i.icon.right.arrow]]]])

(defn app []
  (let [profile  (subscribe [:profile])
        token    (reaction (-> @profile :access_token))
        posts    (subscribe [:posts])]
    (fn []
      [:div.ui.grid.container
       [:div.row [c/search-form @token]]
       [:div.ten.wide.column
        [google-maps/gmap {:id :test
                           :style {:height 300}
                                    :center {:lat -34.397
                                             :lng 150.644}}]]
       [:div.six.wide.column
        [c/posts @posts]]])))

(defn home []
  (let [authorized (subscribe [:is-authorized])]
    (fn []
      (if @authorized
        [app]
        [welcome]))))

(defn profile []
  (let [profile  (subscribe [:profile])
        token    (reaction (:access_token @profile))
        user     (reaction (:user @profile))]
    (fn []
      [:dev.ui.text.container
       [:div.ui.card.centered
        [:div.image
         [:img {:src (:profile_picture @user)}]]
        [:div.content
         [:a.header {:href (str "https://instagram.com/" (:username @user))}
          (:full_name @user)]
         [:div.meta
          (:username @user)]
         [:div.description
          (:bio @user)]]]
       [:h3 "Your access token is"]
       [:code @token]])))

(defn about []
  [:div.ui.text.container
   [:h2 "About Exposure"]
   [:p
    "This is simple application for browsing Instagram data"
    " and build som analytcs around it."]])

(defn active-page []
  (let [active-page (subscribe [:active-page])]
    (fn []
      [:div.ui.container
       (case @active-page
         :home-page [home]
         :profile-page [profile]
         :about-page [about]

         ;; default is like 404
         [:div [:h1 "404?!"]])])))