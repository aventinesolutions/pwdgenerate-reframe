(defproject pwdgenerator-reframe "0.1.2-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.597"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 [thheller/shadow-cljs "2.8.83"]
                 [reagent "0.9.1"]
                 [re-frame "0.11.0"]
                 [secretary "1.2.3"]
                 [compojure "1.6.1"]
                 [yogthos/config "1.1.7"]
                 [ring "1.7.1"]]

  :plugins [
            [lein-less "1.7.5"]
            [lein-shell "0.5.0"]]

  :min-lein-version "2.5.3"

  :jvm-opts ["-Xmx1G"]

  :source-paths ["src/clj" "src/cljs"]

  :test-paths ["test/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]


  :less {:source-paths ["less"]
         :target-path  "resources/public/css"}

  :shell {:commands {"open" {:windows ["cmd" "/c" "start"]
                             :macosx  "open"
                             :linux   "xdg-open"}}}

  :aliases {"dev"          ["with-profile" "dev" "do"
                            ["run" "-m" "shadow.cljs.devtools.cli" "watch" "app"]]
            "prod"         ["with-profile" "prod" "do"
                            ["run" "-m" "shadow.cljs.devtools.cli" "release" "app"]]
            "build-report" ["with-profile" "prod" "do"
                            ["run" "-m" "shadow.cljs.devtools.cli" "run" "shadow.cljs.build-report" "app" "target/build-report.html"]
                            ["shell" "open" "target/build-report.html"]]
            "karma"        ["with-profile" "prod" "do"
                            ["run" "-m" "shadow.cljs.devtools.cli" "compile" "karma-test"]
                            ["shell" "karma" "start" "--single-run" "--reporters" "junit,dots"]
                            ]}

  :profiles
  {:dev
            {:local-repo   "/m2_repository"
             :dependencies [[binaryage/devtools "0.9.11"]
                            [re-frisk "0.5.4.1"]]}

   :prod    {:local-repo "/m2_repository"}

   :uberjar {:source-paths ["env/prod/clj"]
             :omit-source  true
             :main         pwdgenerator-reframe.server
             :aot          [pwdgenerator-reframe.server]
             :uberjar-name "pwdgenerator-reframe.jar"
             :prep-tasks   ["compile" ["prod"] ["less" "once"]]}}

  :prep-tasks [
               ["less" "once"]])
