(ns pwdgenerator-reframe.components.save-personalized-params
  (:require
    [reagent.core :refer [atom]]
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.styles :refer [card-classes]]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.components.login :refer [login]]
    [pwdgenerator-reframe.components.personal-params :refer [personal-params]]))

(defn save-personalized-params []
  (fn []
    (let [user (re-frame/subscribe [::subs/user])]
      [:div#save-personalized-params
       {:class card-classes}
       [:h4.uk-text-primary
        [:span.uk-icon {:data-uk-icon "icon: heart"}]
        " Personalized Params "
        [:span.uk-text-small (:email @user)]]
       (if (nil? @user) [login] [personal-params])])))
