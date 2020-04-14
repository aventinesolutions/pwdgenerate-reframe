(ns pwdgenerator-reframe.components
  (:require
    [pwdgenerator-reframe.styles :refer [card-classes]]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.events :as events]
    ))

(defn save-personalized-params []
  ^{:key :save-personalized-params}
  [:div {:id :save-personalized-params
         :class card-classes}
   [:h4.uk-text-primary "save personalized params"]])
