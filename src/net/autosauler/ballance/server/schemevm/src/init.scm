;; vim: set lispwords+=library,export:

;; Copyright (c) 2006 James Bailey (dgym.REMOVE_THIS.bailey@gmail.com).
;;
;; Permission is hereby granted, free of charge, to any person obtaining a
;; copy of this software and associated documentation files (the "Software"), to
;; deal in the Software without restriction, including without limitation the
;; rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
;; sell copies of the Software, and to permit persons to whom the Software is
;; furnished to do so, subject to the following conditions:
;;
;; The above copyright notice and this permission notice shall be
;; included in all copies or substantial portions of the Software.
;;
;; THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
;; IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
;; FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
;; AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
;; LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
;; OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
;; SOFTWARE.

;;
;; This init file is evaluated just after the built in procedures
;; have been registered. Although everything is in place for
;; libraries, the library macro itself has not been installed yet.
;; Every form here is run in the r6rs library.
;;

;;
;; This boot strapping process is really messy, especially with so little
;; being implemented in the VM. The first goal is to create the (library)
;; macro so that all the usual conveniences of library definitions (such as
;; refering to procedures before they have been defined) are available, but
;; this takes a lot of supporting code in itself.
;;







;;
;; Set up the let macro
;;

(define (list . items)
  items)

(define (null? x) (eq? x '()))

(define (caar x) (car (car x)))
(define (cadr x) (car (cdr x)))
(define (cdar x) (cdr (car x)))
(define (cddr x) (cdr (cdr x)))

(define (caaar x) (car (caar x)))
(define (caadr x) (car (cadr x)))
(define (cadar x) (car (cdar x)))
(define (caddr x) (car (cddr x)))
(define (cdaar x) (cdr (caar x)))
(define (cdadr x) (cdr (cadr x)))
(define (cddar x) (cdr (cdar x)))
(define (cdddr x) (cdr (cddr x)))

(define (caaaar x) (car (caaar x)))
(define (caaadr x) (car (caadr x)))
(define (caadar x) (car (cadar x)))
(define (caaddr x) (car (caddr x)))
(define (cadaar x) (car (cdaar x)))
(define (cadadr x) (car (cdadr x)))
(define (caddar x) (car (cddar x)))
(define (cadddr x) (car (cdddr x)))
(define (cdaaar x) (cdr (caaar x)))
(define (cdaadr x) (cdr (caadr x)))
(define (cdadar x) (cdr (cadar x)))
(define (cdaddr x) (cdr (caddr x)))
(define (cddaar x) (cdr (cdaar x)))
(define (cddadr x) (cdr (cdadr x)))
(define (cdddar x) (cdr (cddar x)))
(define (cddddr x) (cdr (cdddr x)))

(define make-type-predicate
  ((lambda (is-instance?)
     (lambda (type)
       ((lambda (class) 
          (lambda (x)
            (call-method is-instance? class x)))
        (class type))))
   (method "java.lang.Class"
           "isInstance"
           (list
             (class "java.lang.Object")))))

(define symbol? (make-type-predicate "java.lang.String"))
(define pair? (make-type-predicate "net.autosauler.ballance.server.schemevm.Pair"))

;; this is a simple version of map, the full featured map
;; is defined later, when more conveniences have been set up
(define (map fun l)
  (if (null? l)
    '()
    (cons (fun (car l))
          (map fun (cdr l)))))

(add-macro 'let
           (lambda args
             (if (symbol? (car args))
               (begin
                 ;; named let
                 (list (list 'lambda
                             (list (car args))
                             (list 'set! (car args)
                                   (cons 'lambda
                                         (cons (map car (cadr args))
                                               (cddr args))))
                             (cons (car args)
                                   (map cadr (cadr args))))
                       '()))
               (begin
                 ;; unnamed let
                 (cons (cons 'lambda
                             (cons (map car (car args))
                                   (cdr args)))
                       (map cadr (car args)))))))

(add-macro 'let* (lambda (vars . body)
                   (let loop ((vars vars))
                     (if (null? vars)
                       (cons 'let (cons '() body))
                       (list 'let (list (car vars)) (loop (cdr vars)))))))


;;
;; Set up the (and), (cond) and (quasiquote) macros
;;

(define unspecified (let ((unspec (if #f #f))) (lambda () unspec)))

(define (not x) (if x #f #t))

(define (zero? x) (= x 0))

(define (reverse! list)
  (let loop ((list list)
             (last-pair '()))
    (if (null? list)
      last-pair
      (let ((rest (cdr list)))
        (set-cdr! list last-pair)
        (loop rest list)))))

#if (not FAST-LISTS)
(define (append . lists)
  (let app ((curr (car lists))
            (rest (cdr lists))
            (acc ()))
    (if (null? curr)
      (if (null? (cdr rest))
        (if (null? acc)
          (car rest)
          (let ((head (reverse! acc)))
            (set-cdr! acc (car rest))
            head))
        (app (car rest) (cdr rest) acc))
      (app (cdr curr) rest (cons (car curr) acc)))))
#endif

(add-macro 'simple-cond
           (lambda exprs
             (let loop ((exprs exprs))
               (if (null? exprs)
                 (unspecified)
                 (if (eq? (caar exprs) 'else)
                   (cons 'begin (cdar exprs))
                   (cons 'if
                         (cons (caar exprs)
                               (cons (cons 'begin (cdar exprs))
                                     (cons (loop (cdr exprs)) ())))))))))

(add-macro 'and (lambda args
                  (simple-cond
                    ((null? args) #t)
                    ((null? (cdr args)) (car args))
                    (else (list 'if (car args) (cons 'and (cdr args)) #f)))))

(add-macro 'quasiquote
           (lambda (x)
             (let expand ((form x)
                          (nesting 0))
               (simple-cond
                 ((if (symbol? form)
                    #t
                    (null? form))
                  (list 'quote form))
                 ((not (pair? form)) 
                  form)
                 ((eq? (car form) 'unquote)
                  (if (zero? nesting)
                    (cadr form)
                    (list 'list
                          (list 'quote 'unquote)
                          (expand (cadr form) (- nesting 1)))))
                 ((eq? (car form) 'quasiquote)
                  (list 'list
                        (list 'quote 'quasiquote)
                        (expand (cadr form) (+ nesting 1))))
                 ((and (pair? (car form))
                       (eq? (caar form) 'unquote-splicing)
                       (zero? nesting))
                  (list 'append
                        (cadar form)
                        (expand (cdr form) nesting)))
                 (else
                   (list 'cons
                         (expand (car form) nesting)
                         (expand (cdr form) nesting)))))))

(add-macro 'when (lambda (test . body)
                   `(if ,test
                      (begin ,@body))))

(add-macro 'unless (lambda (test . body)
                     `(if (not ,test)
                        (begin ,@body))))

(define method
  (let ((built-in method))
    (lambda (clas name . types)
      (let ((m (built-in clas name (map class types))))
        (lambda args
          (apply call-method m args))))))

(define constructor
  (let ((get-con (method "java.lang.Class" "getConstructor" "[Ljava.lang.Class;"))) 
    (lambda (clas . args)
      (let ((c (get-con (class clas) (make-array "java.lang.Class"
                                                 (map class args)))))
        (lambda args
          (apply call-constructor c args))))))

(define field
  (let ((get-field (method "java.lang.Class" "getField" "java.lang.String"))
        (field-modifiers (method "java.lang.reflect.Field" "getModifiers"))
        (field-ref (method "java.lang.reflect.Field" "get" "java.lang.Object"))
        (field-set! (method "java.lang.reflect.Field" "set" "java.lang.Object" "java.lang.Object"))
        (is-static (method "java.lang.reflect.Modifier" "isStatic" "int")))

    (lambda (clas name)
      (let ((field (get-field (class clas) name)))
        (if (is-static (field-modifiers field))
          (lambda new-val
            (if (pair? new-val)
              (field-set! field '() (car new-val))
              (field-ref field '()))) 
          (lambda (obj . new-val)
            (if (pair? new-val)
              (field-set! field obj (car new-val))
              (field-ref field obj))))))))

(define jstring (constructor "java.lang.String" "[C"))

;;
;; Standard vector functions
;;

(define vector-length (method "java.lang.reflect.Array" "getLength" "java.lang.Object"))
(define vector-ref (method "java.lang.reflect.Array" "get" "java.lang.Object" "int"))
(define vector-set! (method "java.lang.reflect.Array" "set" "java.lang.Object" "int" "java.lang.Object"))

(define (vector->list v)
  (let loop ((i 0)
             (len (vector-length v))
             (acc '()))
    (if (= i len)
      (reverse! acc)
      (loop (+ i 1) len (cons (vector-ref v i) acc)))))

(define (list->vector list)
  (make-array "java.lang.Object" list))

(define (vector . elements)
  (list->vector elements))


;;
;; Standard string functions
;;

(define string? (make-type-predicate "[C"))

(define string<?  #f)
(define string<=? #f)
(define string=?  #f)
(define string>=? #f)
(define string>?  #f)

(define string-ci<?  #f)
(define string-ci<=? #f)
(define string-ci=?  #f)
(define string-ci>=? #f)
(define string-ci>?  #f)

(let* ((compare-to (method "java.lang.String" "compareTo" "java.lang.String"))
       (lower (method "java.lang.String" "toLowerCase"))
       (compare-to-ci (lambda (a b)
                        (compare-to (lower a) (lower b)))))

  (set! string<?  (lambda (a b) (<  (compare-to (jstring a) (jstring b)) 0)))
  (set! string<=? (lambda (a b) (<= (compare-to (jstring a) (jstring b)) 0)))
  (set! string=?  (lambda (a b) (=  (compare-to (jstring a) (jstring b)) 0)))
  (set! string>=? (lambda (a b) (>= (compare-to (jstring a) (jstring b)) 0)))
  (set! string>?  (lambda (a b) (>  (compare-to (jstring a) (jstring b)) 0)))
  (set! string-ci<?  (lambda (a b) (<  (compare-to-ci (jstring a) (jstring b)) 0)))
  (set! string-ci<=? (lambda (a b) (<= (compare-to-ci (jstring a) (jstring b)) 0)))
  (set! string-ci=?  (lambda (a b) (=  (compare-to-ci (jstring a) (jstring b)) 0)))
  (set! string-ci>=? (lambda (a b) (>= (compare-to-ci (jstring a) (jstring b)) 0)))
  (set! string-ci>?  (lambda (a b) (>  (compare-to-ci (jstring a) (jstring b)) 0))))

(define string-length (method "java.lang.reflect.Array" "getLength" "java.lang.Object"))
(define string-ref (method "java.lang.reflect.Array" "getChar" "java.lang.Object" "int"))
(define string-set! (method "java.lang.reflect.Array" "setChar" "java.lang.Object" "int" "char"))

(define string->symbol
  (let ((ns (constructor "java.lang.String" "[C"))
        (intern (method "java.lang.String" "intern")))
    (lambda (str)
      (intern (ns str)))))

(define symbol->string (method "java.lang.String" "toCharArray")) 

(define (substring string start end)
  (let ((len (- end start))
        (res '()))
    (set! res (make-string len))
    (string-blit! string start res 0 len)
    res))

(define (string-copy string)
  (let ((len (string-length string))
        (res '()))
    (set! res (make-string len))
    (string-blit! string 0 res 0 len)
    res))

(define (string-append . parts)
  (let ((len (let loop ((parts parts)
                        (acc 0))
               (if (null? parts)
                 acc
                 (loop (cdr parts) (+ acc (string-length (car parts)))))))
        (res '()))
    (set! res (make-string len))
    (let loop ((parts parts)
               (index 0))
      (if (null? parts)
        res
        (let ((len (string-length (car parts))))
          (string-blit! (car parts) 0 res index len)
          (loop (cdr parts) (+ index len)))))))

(define (list->string chars)
  (let ((res (make-string (length chars))))
    (let loop ((idx 0)
               (chars chars))
      (if (null? chars)
        res
        (begin
          (string-set! res idx (car chars))
          (loop (+ idx 1) (cdr chars)))))))

(define (string->list str)
  (let loop ((idx 0)
             (len (string-length str)))
    (if (eqv? idx len)
      '()
      (cons (string-ref str idx) (loop (+ idx 1) len)))))

(define (string . chars)
  (make-array "char" chars))

(define string->number
  (let ((parseInt (method "java.lang.Integer" "parseInt" "java.lang.String"))
        (parseDouble (method "java.lang.Double" "valueOf" "java.lang.String")))
    (lambda (str)
      (let ((string (jstring str)))
        (try-catch-finally
          (lambda ()
            (parseInt string))
          (lambda ()
            (try-catch-finally
              (lambda ()
                (parseDouble string))
              (lambda ()
                #f)
              #f))
          #f)))))

(define number->string
  (let ((toString (method "java.lang.Number" "toString")))
    (lambda (x)
      (symbol->string (toString x)))))


;;
;; More utilities
;;

(define debug
  (let ((err ((field "java.lang.System" "err")))
        (println (method "java.io.PrintStream" "println" "java.lang.Object")))
    (lambda (x)
      (println err (if (string? x)
                     (jstring x)
                     x))
      x)))

(define gensym
  (let ((counter 0))
    (lambda rest
      (set! counter (+ counter 1))
      (jstring (string-append "__sym__" (number->string counter))))))

(add-macro 'cond (lambda exprs
                   (let ((tmp (gensym)))
                     (let loop ((exprs exprs))
                       (simple-cond
                         ((null? exprs)
                          #f)
                         ((eq? (caar exprs) 'else)
                          `(begin . ,(cdar exprs)))
                         ((null? (cdar exprs))
                          `(let ((,tmp ,(caar exprs)))
                             (if ,tmp
                               ,tmp
                               ,(loop (cdr exprs)))))
                         ((eq? (cadar exprs) '=>)
                          `(let ((,tmp ,(caar exprs)))
                             (if ,tmp
                               (,(caddar exprs) ,tmp)
                               ,(loop (cdr exprs)))))
                         (else
                           `(if ,(caar exprs)
                              (begin . ,(cdar exprs))
                              ,(loop (cdr exprs)))))))))

(add-macro 'or (lambda args
                 (cond
                   ((null? args)
                    #f)
                   ((null? (cdr args))
                    (car args))
                   (else
                     (let ((tmp (gensym)))
                       `(let ((,tmp ,(car args)))
                          (if ,tmp ,tmp
                            ,(let loop ((args (cdr args)))
                               (if (null? (cdr args))
                                 (car args)
                                 `(begin
                                    (set! ,tmp ,(car args))
                                    (if ,tmp ,tmp
                                      ,(loop (cdr args)))))))))))))


;; these utilities allow raw forms to be evaluated - no CPS
;; expansion is performed so this must be done manually which
;; allows explicit control of the continuation
(define full-eval (method "net.autosauler.ballance.server.schemevm.Sixx" "eval" "java.lang.Object" "java.lang.Object" "net.autosauler.ballance.server.schemevm.Library"))

(add-macro 'raw (lambda (form)
                  `(full-eval (interpreter)
                              '()
                              '(identity
                                 ,form)
                              (current-library))))


;;
;; API for java hash tables
;;

;; Since Hashtables don't support char[] keys (scheme strings)
;; they are wrapped in sixx.CharArray.
(define make-char-array (constructor "net.autosauler.ballance.server.schemevm.CharArray" "[C"))
(define char-array? (make-type-predicate "net.autosauler.ballance.server.schemevm.CharArray"))
(define char-array-of (field "net.autosauler.ballance.server.schemevm.CharArray" "charArray"))

(define (ht-wrap-key key)
  (if (string? key)
    (make-char-array key)
    key))

(define (ht-unwrap-key key)
  (if (char-array? key)
    (char-array-of key)
    key))

;; Null values (the empty list) are also not supported, so a special
;; null value is used instead.
(define ht-null (cons #f #f))

(define (ht-wrap-value value)
  (if (null? value)
    ht-null
    value))

(define (ht-unwrap-value value)
  (if (eq? value ht-null)
    '()
    value))


(define make-hash-table (constructor "java.util.Hashtable"))
(define hash-table? (make-type-predicate "java.util.Hashtable"))

(define hash-table-ref #f)
(define hash-table-ref/default #f)
(define hash-table-set! #f)
(define hash-table-exists? #f)
(define hash-table-delete! #f)
(define hash-table-keys #f)
(define hash-table-values #f)
(define hash-table-for-each #f)
(define hash-table-map #f)

(let ((get (method "java.util.Hashtable" "get" "java.lang.Object"))
      (put (method "java.util.Hashtable" "put" "java.lang.Object" "java.lang.Object"))
      (exists (method "java.util.Hashtable" "containsKey" "java.lang.Object"))
      (delete (method "java.util.Hashtable" "remove" "java.lang.Object"))
      (hasMoreElements (method "java.util.Enumeration" "hasMoreElements"))
      (nextElement (method "java.util.Enumeration" "nextElement"))
      (keys (method "java.util.Hashtable" "keys"))
      (elements (method "java.util.Hashtable" "elements")))

  (set! hash-table-ref
    (lambda (ht key)
      (ht-unwrap-value (get ht (ht-wrap-key key)))))

  (set! hash-table-ref/default
    (lambda (ht key . opt-default)
      (let ((res (hash-table-ref ht key)))
        (if (and (null? res) (pair? opt-default))
          (car opt-default)
          res))))

  (set! hash-table-set!
    (lambda (ht key val)
      (put ht (ht-wrap-key key) (ht-wrap-value val))))

  (set! hash-table-exists?
    (lambda (ht key)
      (exists ht (ht-wrap-key key))))

  (set! hash-table-delete!
    (lambda (ht key)
      (delete ht (ht-wrap-key key))))

  (let ((gather (lambda (proc enum)
                  (let loop ((acc '()))
                    (if (hasMoreElements enum)
                      (loop (cons (proc (nextElement enum)) acc))
                      acc)))))

    (set! hash-table-keys
      (lambda (ht)
        (gather ht-unwrap-key (keys ht))))

    (set! hash-table-values
      (lambda (ht)
        (gather ht-unwrap-value (elements ht))))

    (set! hash-table-for-each
      (lambda (ht proc)
        (let loop ((enum (keys ht)) (acc '()))
          (if (hasMoreElements enum)
            (loop enum (let ((key (nextElement enum)))
                         (proc (ht-unwrap-key key)
                               (ht-unwrap-value (get ht key)))))))))

    (set! hash-table-map
      (lambda (ht proc)
        (gather
          (lambda (key)
            (proc (ht-unwrap-key key)
                  (ht-unwrap-value (get ht key))))
          (keys ht))))))


;;
;; the current library is a parameter
;;

(define make-parameter
  (let ((current-thread (method "java.lang.Thread" "currentThread")))
    (lambda (initial-value . opt-converter)
      (let ((thread-locals (make-hash-table))
            (converter (if (pair? opt-converter)
                         (car opt-converter)
                         (lambda (x) x))))
        (set! initial-value (converter initial-value))
        (lambda opt-new-value
          ;;(synchronized
          (if (pair? opt-new-value)
            ;; set new thread specific value
            (begin
              (hash-table-set! thread-locals (current-thread) (converter (car opt-new-value)))
              (unspecified))
            ;; get thread specific value, or global
            (hash-table-ref/default thread-locals (current-thread) initial-value)))))))

(define sixx.libs (field "net.autosauler.ballance.server.schemevm.Sixx" "libs"))
(define get-lib (method "net.autosauler.ballance.server.schemevm.Sixx" "getLib" "java.lang.Object"))

(define (lookup-library name)
  (if (or (symbol? name)
          (pair? name))
    (get-lib (interpreter) name)
    name))

(define current-library (make-parameter 'r6rs lookup-library))
(define interaction-environment current-library)


;;
;; set up the library macro
;;

(define lib-macros (field "net.autosauler.ballance.server.schemevm.Library" "macros"))

(add-macro 'library
           (let ((expand-macros (method "net.autosauler.ballance.server.schemevm.Sixx" "expandMacros" "java.lang.Object" "java.util.Hashtable"))
                 (lib-exports (field "net.autosauler.ballance.server.schemevm.Library" "exports"))
                 (lib-symbol-table (field "net.autosauler.ballance.server.schemevm.Library" "symbolTable"))
                 (lib-slots (field "net.autosauler.ballance.server.schemevm.Library" "slots"))
                 (j-vector-length (method "java.util.Vector" "size"))
                 (j-vector-add (method "java.util.Vector" "addElement" "java.lang.Object"))
                 (import! #f)
                 (ht-copy (lambda (src dst sym)
                            (hash-table-set! dst sym (hash-table-ref/default src sym)))))

             (set! import! (lambda (source target)
                             (let loop ((exports (lib-exports source)))
                               (unless (null? exports)
                                 (cond
                                   ((hash-table-exists? (lib-symbol-table source) (car exports))
                                    (hash-table-set! (lib-symbol-table target) (car exports) (hash-table-ref/default (lib-symbol-table source) (car exports))))
                                   ((hash-table-exists? (lib-macros source) (car exports))
                                    (hash-table-set! (lib-macros target) (car exports) (hash-table-ref/default (lib-macros source) (car exports)))))
                                 (loop (cdr exports))))))
             (lambda (name export import . forms)
               ;; the library form expands into nothing, it is responsible
               ;; for creating/updating the library and running all the
               ;; top level forms at expansion time

               ;; normalize the name, any single names are stored as a list
               (when (symbol? name)
                 (set! name (list name)))

               (let ((lib (get-lib (interpreter) name))
                     (old-lib (current-library)))

                 (when (null? lib)
                   (set! lib ((constructor "net.autosauler.ballance.server.schemevm.Library")))
                   (sixx.libs (interpreter)
                              (cons (cons name lib)
                                    (sixx.libs (interpreter)))))

                 (current-library lib)

                 ;; process the exports
                 (lib-exports lib (cdr export))

                 ;; process the imports
                 (map
                   (lambda (imp)
                     (import! (get-lib (interpreter) imp) lib))
                   (cdr import))
                
                 ;; macro expand the forms
                 (set! forms (map (lambda (form)
                                    (expand-macros (interpreter) form (lib-macros lib)))
                                  forms))

                 ;; allocate all defined slots
                 (let loop ((forms forms))
                   (cond
                     ((null? forms))
                     ((and (pair? (car forms))
                           (eq? (caar forms) 'begin))
                      (loop (cdar forms))
                      (loop (cdr forms)))
                     ((and (pair? (car forms))
                           (eq? (caar forms) 'define))
                      (let ((name (cadar forms))
                            (st (lib-symbol-table lib)))
                        (if (pair? name)
                          (set! name (car name)))
                        (if (hash-table-exists? st name)
                          #f
                          (begin
                            (hash-table-set! st name (cons (lib-slots lib)
                                                           (j-vector-length (lib-slots lib))))
                            (j-vector-add (lib-slots lib) (unspecified)))))
                      (loop (cdr forms)))
                     (else
                       (loop (cdr forms)))))


                 ;; evaluate all forms
                 (full-eval (interpreter) identity (cons 'begin forms) lib)

                 (current-library old-lib))

               #f)))


(library r6rs
  (export
    ;; This list should contain every r6rs defined procedure, ordered and
    ;; categorized as per the r6rs specification. There should be a corresponding
    ;; definition below, or a comment to state that the definition has already
    ;; been made above, is built in, or needs to be done.

    ;; For now this lists every r5rs procedure.and a few others known to
    ;; be in r6rs

    ;; primitive expression types
    set! if
    define-macro
    ;; derived expression types
    cond
    case
    and
    or
    let
    let*
    letrec*
    letrec
    do
    quasiquote
    ;; equivalence predicates
    eq?  eqv? equal?
    ;; maths and numbers
    number? complex? real? rational? integer?
    exact? inexact?
    = < > <= >=
    zero? positive? negative? odd? even?
    max min
    + * - /
    abs
    quotient remainder modulo ; these are being dropped
    gcd lcm
    numerator denominator
    floor ceiling truncate round
    rationalize
    exp log sin cos tan asin acos atan
    sqrt expt
    make-rectangular make-polar real-part imag-part magnitude angle
    exact->inexact inexact->exact
    number->string string->number
    ;; booleans
    not
    boolean?
    ;; pairs and lists
    pair?
    cons car cdr set-car!  set-cdr!
    caar cadr cdar cddr
    caaar caadr cadar caddr cdaar cdadr cddar cdddr
    caaaar caaadr caadar caaddr cadaar cadadr caddar cadddr cdaaar cdaadr cdadar cdaddr cddaar cddadr cdddar cddddr
    null?
    list?
    list
    length
    append
    reverse
    list-tail
    list-ref
    memq memv member
    assq assv assoc
    ;; symbols
    symbol?
    symbol->string
    string->symbol
    ;; characters
    char?
    char=? char<? char>? char<=? char>=?
    char-ci=? char-ci<? char-ci>? char-ci<=? char-ci>=?
    char-alphabetic? char-numeric? char-whitespace? char-upper-case? char-lower-case?
    char->integer
    integer->char
    char-upcase
    char-downcase
    ;; strings
    string?
    make-string
    string
    string-length
    string-ref
    string-set!
    string=? string-ci=?
    string<? string>? string<=? string>=?
    string-ci<? string-ci>? string-ci<=? string-ci>=?
    substring
    string-append
    string->list
    list->string
    string-copy
    string-fill!
    ;; vectors
    vector?
    make-vector
    vector
    vector-length
    vector-ref
    vector-set!
    vector->list
    list->vector
    ;; control features
    procedure?
    apply
    map
    for-each
    force delay ; these are being dropped
    call/cc
    call-with-current-continuation
    values
    call-with-values
    dynamic-wind
    eval
    ;; ports
    call-with-input-file call-with-output-file
    input-port? output-port?
    current-input-port current-output-port
    with-input-from-file with-output-to-file
    open-input-file open-output-file
    close-input-port close-output-port
    ;; input
    read
    read-char
    peek-char
    eof-object?
    char-ready?
    ;; output
    write
    display
    newline
    write-char
    ;; system interface
    load
    ;; known r6rs exports
    unspecified
    eof-object
    when
    unless
    library)

  (import)

  
  ;;
  ;; The definitions. Some have already been defined above or are
  ;; built in.
  ;;

  (add-macro 'define-macro
             (lambda (name+args . body)
               (hash-table-set!
                 (lib-macros (current-library))
                 (car name+args)
                 (full-eval (interpreter)
                            identity
                            `(lambda ,(cdr name+args)
                               . ,body)
                            (current-library)))
               #f))


  ;;
  ;; derived expression types
  ;;

  (add-macro 'case (lambda (var . exprs)
                     (let ((tmp (gensym)))
                       `(let ((,tmp ,var))
                          ,(let loop ((exprs exprs))
                             (cond
                               ((null? exprs)
                                '(if #f #f))
                               ((eq? (caar exprs) 'else)
                                `(begin . ,(cdar exprs)))
                               (else
                                 `(if (memv ,tmp ',(caar exprs))
                                    (begin . ,(cdar exprs))
                                    ,(loop (cdr exprs))))))))))

  (add-macro 'letrec* (lambda (vars . body)
                        `((lambda ,(map car vars)
                            ,@(map (lambda (var)
                                     (cons 'set! var))
                                   vars)
                            . ,body)
                          . ,(map (lambda (x) #f) vars))))

  (add-macro 'letrec (lambda args
                       `(letrec* . ,args)))

  (add-macro 'do (lambda (bindings test-and-result . body)
                   (let ((variables (map (lambda (clause)
                                           (list (car clause) (cadr clause)))
                                         bindings))
                         (steps (map (lambda (clause)
                                       (if (null? (cddr clause))
                                         (car clause)   
                                         (caddr clause)))
                                     bindings))
                         (test (car test-and-result))
                         (result (cdr test-and-result))
                         (loop (gensym)))
                     `(let ,loop ,variables
                        (if ,test
                          (begin ,(if (null? result)
                                    '(unspecified)
                                    (car result)))
                          (begin
                            ,@body
                            (,loop ,@steps)))))))

  ;;
  ;; equivalence predicates
  ;;


  ;;
  ;; maths and numbers
  ;;

  (define (integer? x)
    (if (exact? x)
      #t
      (inexact? x)))

  (define rational? integer?)
  (define real? rational?)
  (define complex? real?)
  (define number? complex?)

  (define exact? (make-type-predicate "java.lang.Integer"))
  (define inexact? (make-type-predicate "java.lang.Double"))

  (define (zero? x)
    (= x 0))

  (define (positive? x)
    (> x 0))

  (define (negative? x)
    (< x 0))

  (define (odd? x)
    (= (modulo x 2) 1))

  (define (even? x)
    (= (modulo x 2) 0))

  (define (min . args)
    (let loop ((min (car args))
               (rest (cdr args)))
      (cond
        ((null? rest)
         min)
        ((< (car rest) min)
         (loop (car rest) (cdr rest)))
        (else
          (loop min (cdr rest))))))

  (define (max . args)
    (let loop ((max (car args))
               (rest (cdr args)))
      (cond
        ((null? rest)
         max)
        ((> (car rest) max)
         (loop (car rest) (cdr rest)))
        (else
          (loop max (cdr rest))))))

  (define (abs x)
    (if (< x 0)
      (- x)
      x))

  ;; TODO: quotient remainder
  ;; TODO: gcd lcm
  ;; TODO: numerator denominator
  (define floor (method "java.lang.Math" "floor" "double"))
  (define ceiling (method "java.lang.Math" "ceil" "double"))
  ;; TODO: truncate round
  ;; TODO: rationalize

  (define exp (method "java.lang.Math" "exp" "double"))
  (define log (method "java.lang.Math" "log" "double"))

  (define sin (method "java.lang.Math" "sin" "double"))
  (define cos (method "java.lang.Math" "cos" "double"))
  (define tan (method "java.lang.Math" "tan" "double"))

  (define asin (method "java.lang.Math" "asin" "double"))
  (define acos (method "java.lang.Math" "acos" "double"))

  (define atan1 (method "java.lang.Math" "atan" "double"))
  (define atan2 (method "java.lang.Math" "atan2" "double" "double"))
  (define (atan x . opt-y)
    (if (pair? opt-y)
      (atan2 x (car opt-y))
      (atan1 x)))

  (define sqrt (method "java.lang.Math" "sqrt" "double"))
  (define expt (method "java.lang.Math" "pow" "double" "double"))

  ;; TODO: make-rectangular make-polar real-part imag-part magnitude angle

  (define exact->inexact (method "java.lang.Number" "doubleValue"))
  (define inexact->exact (method "java.lang.Number" "intValue"))


  ;; defined above: number->string string->number

  ;;
  ;; booleans
  ;;

  ;; defined above: not

  (define boolean? (make-type-predicate "java.lang.Boolean"))

  ;;
  ;; pairs and lists
  ;;

  ;; list? is built in
  ;; list is defined above
  ;; length is built in

#if (not FAST-LISTS)
  (define (reverse list)
    (let loop ((list list)
               (acc '()))
      (if (null? list)
        acc
        (loop (cdr list) (cons (car list) acc)))))

  (define (list-tail list count)
    (if (zero? count)
      list
      (list-tail (cdr list) (- count 1))))
#endif

  (define (list-ref list count)
    (car (list-tail list count)))

  (define (memq item list)
    (cond
      ((null? list)
       #f)
      ((eq? item (car list))
       list)
      (else (memq item (cdr list)))))

  (define (memv item list)
    (cond
      ((null? list)
       #f)
      ((eqv? item (car list))
       list)
      (else (memv item (cdr list)))))

  (define (member item list)
    (cond
      ((null? list)
       #f)
      ((equal? item (car list))
       list)
      (else (member item (cdr list)))))

  (define (assq item list)
    (cond
      ((null? list)
       #f)
      ((eq? item (caar list))
       (car list))
      (else (assq item (cdr list)))))

  (define (assv item list)
    (cond
      ((null? list)
       #f)
      ((eqv? item (caar list))
       (car list))
      (else (assv item (cdr list)))))

  (define (assoc item list)
    (cond
      ((null? list)
       #f)
      ((equal? item (caar list))
       (car list))
      (else (assoc item (cdr list)))))

  ;;
  ;; symbols
  ;;

  ;;
  ;; characters
  ;;

  (define char? (make-type-predicate "java.lang.Character"))

  (define (char=? a b) (= (char->integer a) (char->integer b)))
  (define (char<? a b) (< (char->integer a) (char->integer b)))
  (define (char>? a b) (> (char->integer a) (char->integer b)))
  (define (char<=? a b) (<= (char->integer a) (char->integer b)))
  (define (char>=? a b) (>= (char->integer a) (char->integer b)))

  (define (char-ci=? a b) (char=? (char-downcase a) (char-downcase b)))
  (define (char-ci<? a b) (char<? (char-downcase a) (char-downcase b)))
  (define (char-ci>? a b) (char>? (char-downcase a) (char-downcase b)))
  (define (char-ci<=? a b) (char<=? (char-downcase a) (char-downcase b)))
  (define (char-ci>=? a b) (char>=? (char-downcase a) (char-downcase b)))

  (define char-alphabetic? (method "java.lang.Character" "isLetter" "char"))
  (define char-numeric? (method "java.lang.Character" "isDigit" "char"))
  (define char-whitespace? (method "java.lang.Character" "isWhitespace" "char"))
  (define char-upper-case? (method "java.lang.Character" "isUpperCase" "char"))
  (define char-lower-case? (method "java.lang.Character" "isLowerCase" "char"))

  (define char->integer (constructor "java.lang.Integer" "int"))
  ;; built in: integer->char

  (define char-upcase (method "java.lang.Character" "toUpperCase" "char"))
  (define char-downcase (method "java.lang.Character" "toLowerCase" "char"))

  ;;
  ;; strings
  ;;

  ;;
  ;; vectors
  ;;

  (define vector? (make-type-predicate "[Ljava.lang.Object;"))

  (define (make-vector k . opt-fill)
    (let ((fill (if (pair? opt-fill)
                  (car opt-fill)
                  (unspecified))))
      (list->vector (let loop ((idx 0))
                      (if (= idx k)
                        '()
                        (cons fill (loop (+ idx 1))))))))

  ;;
  ;; control features
  ;;

  (define procedure? (make-type-predicate "net.autosauler.ballance.server.schemevm.Procedure"))

  (define for-each #f)

  (let ((map-into (lambda (fun src dst)
                    (let loop ((src src) (dst dst))
                      (unless (null? src)
                        (set-car! dst (fun (car src)))
                        (loop (cdr src) (cdr dst))))
                    dst))

        (make-blank-copy (lambda (l)
                           (let loop ((l l) (acc '()))
                             (if (null? l)
                               acc
                               (loop (cdr l) (cons #f acc)))))))

    (set! map (lambda (fun . lists)
                (cond
                  ((null? (cdr lists))
                   ;; single list
                   (map-into fun (car lists) (make-blank-copy (car lists))))
                  (else
                    ;; slower multi-list version
                    (let ((cars (make-blank-copy lists))
                          (results (make-blank-copy (car lists))))
                      (let loop ((remaining results))
                        (if (null? remaining)
                          results
                          (begin
                            (set-car! remaining (apply fun (map-into car lists cars)))
                            (map-into cdr lists lists)
                            (loop (cdr remaining))))))))))

    (set! for-each (lambda (fun . lists)
                     (cond
                       ((null? (cdr lists))
                        ;; single list
                        (let loop ((list (car lists)))
                          (if (null? list)
                            (if #f #f)
                            (begin (fun (car list))
                                   (loop (cdr list))))))
                       (else
                         ;; slower multi-list version
                         (let ((cars (make-blank-copy lists)))
                           (let loop ()
                             (if (null? (car lists))
                               (if #f #f)
                               (begin
                                 (apply fun (map-into car lists cars))
                                 (map-into cdr lists lists)
                                 (loop))))))))))


  (define call/cc (raw (lambda (cont proc)
                         (proc cont (lambda (cont- res) (cont res))))))
  (define call-with-current-continuation call/cc)

  (define call-with-values
    (raw (lambda (cont generator consumer)
           (generator (lambda values
                        (apply cont consumer values))))))

  ;; TODO: dynamic-wind

  (define eval
    (raw (lambda (cont form lib)
           (interpreter
             (lambda (i)
               (lookup-library
                 (lambda (l)
                   (full-eval identity i cont form l))
                 lib))))))

  ;;
  ;; ports
  ;;

  #if FILE-IO

  (define (call-with-input-file name proc)
    (let ((port (open-input-file name)))
      (try-catch-finally
        (lambda ()
          (proc port))
        #f
        (lambda ()
          (close-input-port port)))))

  (define (call-with-output-file name proc)
    (let ((port (open-output-file name)))
      (try-catch-finally
        (lambda ()
          (proc port))
        #f
        (lambda ()
          (close-output-port port)))))


  (define input-port? (make-type-predicate "java.io.BufferedReader"))
  (define output-port? (make-type-predicate "java.io.Writer"))

  #endif

  (define new-buffered-reader (constructor "java.io.BufferedReader"
                                           "java.io.Reader"))
  (define new-buffered-writer (constructor "java.io.BufferedWriter"
                                           "java.io.Writer"))


  (define current-input-port 
    (make-parameter (new-buffered-reader
                      ((constructor "java.io.InputStreamReader"
                                    "java.io.InputStream")
                       ((field "java.lang.System" "in"))))))

  (define current-output-port
    (make-parameter (new-buffered-writer
                      ((constructor "java.io.OutputStreamWriter"
                                    "java.io.OutputStream")
                       ((field "java.lang.System" "out"))))))


  #if FILE-IO

  (define (with-input-from-file name thunk)
    (let ((orig (current-input-port))
          (port (open-input-file name)))
      (try-catch-finally
        (lambda ()
          (current-input-port port)
          (thunk))
        #f
        (lambda ()
          (current-input-port orig)
          (close-input-port port)))))

  (define (with-output-to-file name thunk)
    (let ((orig (current-output-port))
          (port (open-output-file name)))
      (try-catch-finally
        (lambda ()
          (current-output-port port)
          (thunk))
        #f
        (lambda ()
          (current-output-port orig)
          (close-output-port port)))))


  (define (open-input-file name)
    (new-buffered-reader
      ((constructor "java.io.FileReader" "java.lang.String") name)))

  (define (open-output-file name)
    (new-buffered-writer
      ((constructor "java.io.FileWriter" "java.lang.String") name)))


  (define close-input-port (method "java.io.Reader" "close"))
  (define close-output-port (method "java.io.Writer" "close"))

  #endif

  ;;
  ;; input
  ;;

  (define eof-object
    (let ((eof (jstring "#<eof>")))
      (lambda ()
        eof)))

  (define read
    (let ((new-reader (constructor "net.autosauler.ballance.server.schemevm.Reader" "java.io.BufferedReader"))
          (read (method "net.autosauler.ballance.server.schemevm.Reader" "read")))
      (lambda opt-input-port
        (try-catch-finally
          (lambda ()
            (read (new-reader (if (pair? opt-input-port)
                                (car opt-input-port)
                                (current-input-port)))))
          (lambda (e)
            (eof-object))
          #f))))

  (define read-char
    (let ((read (method "java.io.Reader" "read")))
      (lambda opt-input-port
        (let ((result (read (if (pair? opt-input-port)
                              (car opt-input-port)
                              (current-input-port)))))
          (if (= result -1)
            (eof-object)
            (integer->char result))))))

  (define peek-char
    (let ((read (method "java.io.Reader" "read"))
          (mark (method "java.io.Reader" "mark" "int"))
          (reset (method "java.io.Reader" "reset")))
      (lambda opt-input-port
        (let ((input-port (if (pair? opt-input-port)
                            (car opt-input-port)
                            (current-input-port)))
              (result #f))
          (mark input-port 1)
          (set! result (read input-port))
          (reset input-port)
          (if (= result -1)
            (eof-object)
            (integer->char result))))))

  (define (eof-object? x)
    (eq? x (eof-object)))

  (define char-ready?
    (let ((ready (method "java.io.Reader" "ready")))
      (lambda opt-input-port
        (ready (if (pair? opt-input-port)
                 (car opt-input-port)
                 (current-input-port))))))

  ;;
  ;; output
  ;;

  (define write-char
    (let ((write (method "java.io.Writer" "write" "int")))
      (lambda (char . opt-output-port)
        (write (if (pair? opt-output-port)
                 (car opt-output-port)
                 (current-output-port))
               char)
        (unspecified))))

  (define write-string
    (let ((write (method "java.io.Writer" "write" "[C")))
      (lambda (string . opt-output-port)
        (write (if (pair? opt-output-port)
                 (car opt-output-port)
                 (current-output-port))
               string)
        (unspecified))))

  (define flush-output
    (let ((flush (method "java.io.Writer" "flush")))
      (lambda opt-output-port
        (flush (if (pair? opt-output-port)
                 (car opt-output-port)
                 (current-output-port)))
        (unspecified))))

  (define (newline . opt-output-port)
    (let ((output-port (if (pair? opt-output-port)
                         (car opt-output-port)
                         (current-output-port))))
      (write-char #\newline output-port)
      (flush-output output-port)))

  ;; these writers will run forever on cyclic structures
  (define write #f)
  (define display #f)
  ;; these will stop (and are aliases)
  (define write-with-shared-structure #f)
  (define write/ss #f)

  (letrec ((to-string (method "java.lang.Object" "toString"))
           (output (lambda (form readable? seen-list out)
                     (let ((cyclic (assq form seen-list)))
                       (when cyclic
                         (write-char #\# out)
                         (output (caddr cyclic) #t '() out))
                       (cond
                         ((and cyclic
                               (cadr cyclic))
                          ;; cyclic and has been printed
                          (write-char #\# out))
                         ((null? form)
                          (write-string "()" out))
                         ((eq? form #t)
                          (write-string "#t" out))
                         ((eq? form #f)
                          (write-string "#f" out))
                         ((char? form)
                          (cond
                            ((and readable?
                                  (char=? form #\space))
                             (write-string "#\\space" out))
                            ((and readable?
                                  (char=? form #\newline))
                             (write-string "#\\newline" out))
                            (readable?
                              (write-string "#\\" out)
                              (write-char form out))
                                  (else
                                    (write-char form out))))
                            ((number? form)
                             (write-string (number->string form) out))
                            ((symbol? form)
                             (write-string (symbol->string form) out))
                            ((string? form)
                             (cond
                               (readable?
                                 (write-char #\" out)
                                 (let ((len (string-length form))
                                       (c #f))
                                   (let loop ((idx 0))
                                     (unless (eqv? idx len)
                                       (set! c (string-ref form idx))
                                       (if (or (char=? c #\\ ) (char=? c #\"))
                                         (write-char #\\ out))
                                       (write-char c out)
                                       (loop (+ idx 1)))))
                                 (write-char #\" out))
                               (else
                                 (write-string form out))))
                            ((pair? form)
                             (write-char #\( out)
                             (output (car form) readable? seen-list out)
                             (let loop ((form (cdr form)))
                               (cond
                                 ((pair? form)
                                  (write-char #\space out)
                                  (output (car form) readable? seen-list out)
                                  (loop (cdr form)))
                                 ((null? form)
                                  (write-char #\) out))
                                 (else
                                   (write-string " . " out)
                                   (output form readable? seen-list out)
                                   (loop '())))))
                            ((vector? form)
                             (write-char #\# out)
                             (output (vector->list form) readable? seen-list out))
                            (else
                              (write-string "#<unknown " out)
                              (write-string (symbol->string (to-string form)) out)
                              (write-string ">" out)))))))

    (set! write
      (lambda (form . opt-output-port)
        (output form #t '() (if (pair? opt-output-port)
                              (car opt-output-port)
                              (current-output-port)))))

    (set! display
      (lambda (form . opt-output-port)
        (output form #f '() (if (pair? opt-output-port)
                              (car opt-output-port)
                              (current-output-port))))))

  ;;
  ;; system interface
  ;;

  (define load
    (let ((load-from-jar (method "net.autosauler.ballance.server.schemevm.Sixx" "loadFromJar" "java.lang.String" "net.autosauler.ballance.server.schemevm.Library")))
      (lambda (filename)
        (load-from-jar (interpreter) filename (current-library)))))

  (set! debug
    (lambda (x)
      (write x)(newline)
      x))
  )

;;
;; Now that r6rs is defined, it is necessary to define the sixx
;; module that provides all the low level access that isn't part
;; of the standard. There is a problem that these internal definitions
;; are unaccessible to any other library.
;;
;; For what it is worth I don't like these things being completely
;; hidden, it is an artificial barrier. In Common Lisp a package's
;; non-exported symbols are actually available, as is pretty much
;; everything in Python, and I feel this flexibility is very useful.
;;
;; This is why I see no problem in making the internals available,
;; and they are thanks to (eval).
;;
;; dgym
;;


(library sixx
  (export 
    class constructor method field
    try-catch-finally
    throw
    thread
    make-parameter
    gensym
    interpreter
    current-library
    make-type-predicate
    make-array
    debug
    jstring
    jfloat
    jbyte
    write-string
    flush-output
    synchronized
    error
    argv)

  (import r6rs)


  (define-macro (imp . names)
    `(begin
       . ,(map (lambda (name)
                 `(define ,name (eval ',name 'r6rs)))
               names)))

  (imp class
       constructor
       method
       field
       try-catch-finally
       throw
       make-parameter
       gensym
       interpreter
       current-library
       make-type-predicate
       make-array
       jstring
       write-string
       flush-output
       synchronized
       debug)

  (define thread
    (let ((make-sixx-thread (constructor "net.autosauler.ballance.server.schemevm.SixxThread" "net.autosauler.ballance.server.schemevm.Sixx" "java.lang.Object")))
      (lambda (thunk)
        (make-sixx-thread (interpreter) thunk))))

  (define jfloat (constructor "java.lang.Float" "double"))
  (define jbyte (constructor "java.lang.Byte" "byte"))
  
  (define new-exception (constructor "java.lang.Exception" "java.lang.String"))
  (define (error string . rest)
    (let ((port ((constructor "java.io.StringWriter"))))
      (display string port)
      (for-each (lambda (item)
                  (display " " port)
                  (write item port))
                rest)
      (newline port)
      (throw (new-exception ((method "java.lang.Object" "toString") port)))))
  
  (define argv '()))


;;
;; hashtables library
;;

(library hashtables
  (export
    hash-table? make-hash-table
    hash-table-ref hash-table-ref/default
    hash-table-set! hash-table-exists? hash-table-delete!
    hash-table-keys hash-table-values
    hash-table-map hash-table-for-each)

  (import r6rs
          sixx)


  (define-macro (imp . names)
    `(begin
       . ,(map (lambda (name)
                 `(define ,name (eval ',name 'r6rs)))
               names)))

  (imp
    hash-table? make-hash-table
    hash-table-ref hash-table-ref/default
    hash-table-set! hash-table-exists? hash-table-delete!
    hash-table-keys hash-table-values
    hash-table-map hash-table-for-each))


;;
;; r6rs fixnums
;;

#if BITWISE-OPS
(library (r6rs arithmetic fixnum)
  (export
    #if BITWISE-OPS
    fixnum-not
    fixnum-and
    fixnum-ior
    fixnum-xor
    #endif
    )

  (import r6rs)

  #if BITWISE-OPS
  (define bitwise (eval 'bitwise 'r6rs))

  (define (fixnum-not fx)
    (bitwise #\~ fx))

  (define (fixnum-and fx1 fx2)
    (bitwise #\& fx1 fx2))

  (define (fixnum-ior fx1 fx2)
    (bitwise #\| fx1 fx2))

  (define (fixnum-xor fx1 fx2)
    (bitwise #\^ fx1 fx2))
  #endif
  )
#endif


;;
;; (r6rs i/o ports)
;;

#if STRING-IO
(library (r6rs i/o ports)
  (export
    #if STRING-IO
    open-string-input-port
    #endif
    )

  (import r6rs sixx)

  (define new-buffered-reader (constructor "java.io.BufferedReader"
                                           "java.io.Reader"))
  (define new-string-reader (constructor "java.io.StringReader"
                                         "java.lang.String"))

  (define (open-string-input-port str)
    (new-buffered-reader (new-string-reader str)))
  )
#endif

