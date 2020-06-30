(ns pwdgenerator-reframe.components.personal-params
  (:require
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.styles :refer [card-classes]]
    [pwdgenerator-reframe.events :as events]
    [pwdgenerator-reframe.subs :as subs]
    [com.degel.re-frame-firebase :as firebase]))

(def document-path [:params])

(defn personal-params []
  (fn []
    (let [params (re-frame/subscribe [:firestore/on-snapshot {:path-collection [:params]}])]
      ^{:key :personal-params}
      [:div#personal-params
       [:div.uk-text-small (pr-str @params)]
       [:fieldset#logoff
        {:class [:uk-form-stacked :uk-padding-small]}
        [:button {:class [:uk-button :uk-button-primary :uk-button-small]
                  :on-click
                         (fn [event]
                           (.preventDefault event)
                           (re-frame/dispatch [::events/sign-out]))} "logoff"]]])))
