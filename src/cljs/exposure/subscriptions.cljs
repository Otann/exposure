(ns exposure.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

(re-frame/register-sub :active-page
  (fn [db _] (reaction (:active-page @db))))

(re-frame/register-sub :profile
  (fn [db _] (reaction (:profile @db))))

(re-frame/register-sub :is-authorized
  (fn [db _] (reaction (boolean (:profile @db)))))