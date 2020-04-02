(ns pwdgenerator-reframe.domain
  (:require
    [clojure.string :refer [join]]))

(def password-validations
  [["At least 12 characters"
    (fn [s]
      (>= (count s) 12))]
   ["At least 50% unique characters"
    (fn [s]
      (-> s
          set
          count
          (/ (count s))
          (>= 0.5)))]])

(defn random-char [s]
  (nth s (rand-int (count s))))

(defn random-string [len s]
  (let [length (js/parseInt len)]
    (apply str (take length (repeatedly #(random-char s))))))

(defn uppercase-word [params]
  (random-string (:no_uppercase_alpha params) (:uppercase params)))

(defn lowercase-word [params]
  (random-string (:no_lowercase_alpha params) (:lowercase params)))

(defn numerics-symbols-word [params]
  (apply str
         (shuffle
           (seq
             (str (random-string (:no_numerics params) (:numerics params))
                  (random-string (:no_symbols params) (:symbols params)))))))

(defn all-words [params]
  (let [generators [uppercase-word lowercase-word numerics-symbols-word]
        words (js/parseInt (:no_words params))]
    (shuffle
      (take words (repeatedly #((nth generators (rand-int (count generators))) params))))))

(defn generate-pw [params]
  (join (:word_separator params) (all-words params)))
