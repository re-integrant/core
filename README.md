# re-integrant

A Clojure library designed to create a module of re-integrant pattern.
Please read [this post](https://223kazuki.github.io/re-integrant-app.html) about this pattern.

## Installation
To install, add the following to your project :dependencies:

```clojure
[re-integrant "0.1.0-SNAPSHOT"]
```

## Usage

In order to define re-integrant module, you have to define re-frame handlers and integrant keys for the module.

```clojure:module/example_module.cljs
(require '[integrant.core :as ig]
          '[re-integrant.core :as re-integrant :refer-macros [defevent defsub deffx]])

(defmulti reg-sub identity)
(defsub reg-sub ::loading?
  (fn [db _]
    ;; ...
    ))

;; Events
(defmulti reg-event identity)
(defevent reg-event ::init
  (fn [db _]
    ;; ...
    ))
(defevent reg-event ::halt
  (fn [db _]
    ;; ...
    ))

(defmethod ig/init-key :test-app.module/example-module [k opts]
  (re-integrant/init-module
    {:reg-sub reg-sub :reg-event reg-event
     :init-event [::init] :halt-event [::halt]
     :container container}))

(defmethod ig/halt-key! :test-app.module/example-module [k module]
  (re-integrant/halt-module module))
```

Then you can initialize system as bellow.

```clojure:config.cljs
(require '[integrant.core :as ig]
         '[test-app.module.example-module])

(ig/init {:test-app.module/example-module {}})
```

## License

Copyright © 2019 Kazuki Tsutsumi

Distributed under the MIT license.