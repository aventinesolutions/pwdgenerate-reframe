(ns pwdgenerator-reframe.views
  (:require
    [cljs.pprint :refer [pprint]]
    [re-frame.core :as re-frame]
    [pwdgenerator-reframe.domain :refer [password-validations]]
    [pwdgenerator-reframe.subs :as subs]
    [pwdgenerator-reframe.events :as events]
    [uikit]))

;; home

(def card-classes [:uk-width-auto :uk-padding-small :uk-margin
                   :uk-card :uk-card-body :uk-card-default :uk-margin-left :uk-responsive-width
                   :uk-box-shadow-medium :uk-box-shadow-hover-large])

(defn on-field-change [params field event]
  (re-frame/dispatch [::events/params (assoc params field (-> event .-target .-value))]))

(defn form-field [field]
  (let [params (re-frame/subscribe [::subs/params])
        field-defs (re-frame/subscribe [::subs/field-defs])
        defs (field @field-defs)]
    ^{:key field}
    [:div {:id    (str field "-input")
           :class card-classes}
     [:label {:class [:uk-text-primary]} (:label defs)
      [:input {:class     [:uk-input]
               :type      :text
               :size      (:size defs)
               :maxLength (:maxlength defs)
               :value     (field @params)
               :on-change #(on-field-change @params field %)}]]]))

(defn form-fields []
  (let [field-defs (re-frame/subscribe [::subs/field-defs])]
    (map #(form-field %) (sort-by #(:order (% @field-defs)) (keys @field-defs)))))

(defn pwdgenerator []
  (let [value (re-frame/subscribe [::subs/value])
        params (re-frame/subscribe [::subs/params])
        show? (re-frame/subscribe [::subs/show?])
        dirty? (re-frame/subscribe [::subs/dirty?])
        focus? (re-frame/subscribe [::subs/focus?])]
    (fn []
      (let [validations (for [[desc f] password-validations]
                          [desc (f (:value @value))])
            valid? (every? identity (map second validations))
            color (when @dirty? (if valid? "green" "red"))]

        [:div {:id    :form-container "uk-grid" 1
               :class [:uk-grid-large :uk-background-default :uk-padding-small]}
         [:form
          ^{:key :password}
          [:div {:id "password-container" :class card-classes}
           [:label {:class [:uk-text-lead :uk-text-bolder :uk-text-primary]
                    :style {:color color}} "Password"]
           [:input {:class     [:uk-input]
                    :type      (if @show? :text :password)
                    :style     {:border (str "1px solid " color)}
                    :value     @value
                    :on-focus  #(re-frame/dispatch [::events/focus? true])
                    :on-blur   #(re-frame/dispatch [::events/dirty? true])
                    :on-change #(doall (re-frame/dispatch [::events/dirty? true])
                                       (re-frame/dispatch [::events/value (-> % .-target .-value)]))}]
           (doall
             (for [[desc valid?] validations]
               (when focus?
                 ^{:key desc}
                 [:div {:style {:color (when @dirty?
                                         (if valid? "green" "red"))}}
                  (when @dirty? (if valid? "✔ " "✘ "))
                  desc])))]
          [:div {:id :button-group :class card-classes}
           ^{:key "regenerate"}
           [:button {:id    :regenerate
                     :class [:uk-button :uk-width-1-2 :uk-button-small :uk-button-primary
                             :uk-box-shadow-medium :uk-box-shadow-hover-large]
                     :on-click
                            (fn [event]
                              (do
                                (.preventDefault event)
                                (re-frame/dispatch [::events/generate])))} "Regenerate"]
           ^{:key "reset"}
           [:button {:id    :reset
                     :class [:uk-button :uk-width-1-2 :uk-button-small :uk-button-secondary
                             :uk-box-shadow-medium :uk-box-shadow-hover-large]
                     :on-click
                            (fn [event]
                              (do
                                (.preventDefault event)
                                (re-frame/dispatch [::events/reset])))} "Reset"]]
          ^{:key "show-password-input"}
          [:div {:id    "show-password-input"
                 :class card-classes}
           [:label {:class [:uk-text-primary]}
            [:input {:class     :uk-checkbox
                     :type      :checkbox
                     :checked   @show?
                     :on-change #(re-frame/dispatch [::events/show? (-> % .-target .-checked)])}]
            " Show password?"]]
          (doall (form-fields))
          ^{:key "word-separator-input"}
          [:div {:id    "word-separator-input"
                 :class card-classes}
           [:label {:class [:uk-text-primary]}
            "Word Separator "
            [:input {:type      :text
                     :size      3
                     :maxLength 3
                     :value     (:word_separator @params)
                     :on-change #(on-field-change @params :word_separator %)}]
            " " (pr-str (:word_separator @params))]]
          ]]))))

(defn home-panel []
  (let [name @(re-frame/subscribe [::subs/name])]
    [:div {:class [:uk-background-muted :uk-flex :uk-flex-column :uk-flex-around
                   :uk-flex-top :uk-padding]}
     [:div {:class [:uk-background-secondary :uk-light]}
      [:h1 {:class [:uk-padding]} name]]
     [pwdgenerator]

     [:div {:class [:uk-text-small :uk-padding]}
      [:a {:class [:uk-link-muted] :href "#/about"}
       (str "About \"" name "\"")]]
     ]))

;; about

(defn about-panel []
  [:div
   [:h1 "This is the About Page."]

   [:div
    [:a {:href "#/"}
     "go to Home Page"]]])

;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (.grid uikit (.getElementById js/document "form-container"))
    [show-panel @active-panel]))
