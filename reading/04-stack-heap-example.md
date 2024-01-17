# Stack/Heap Example

Now that we've covered the concepts, let's walk through each line of the
`MemoryExample` code we saw before , and watch as the stack and heap get
updated over time.

1. Execution has started inside the `main()` method. The `MemoryExample`
variable, `me` has been assigned a value of `null`. `me` is a reference
to no object, as `null` is a special "address" meaning no object. So
we'll represent that as `null` instead of having a pointer to somewhere
on the heap.
  ![Execution has started inside the `main()` method. The `MemoryExample` variable, `me` has been assigned a value of `null`. `me` is a reference to no object, as `null` is a special "address" meaning no object. So we'll represent that as `null` instead of having a pointer to somewhere on the heap.](Memory-Line-by-line-01.png)

1. A new `MemoryExample` object has been allocated on the heap and
instantiated. The local variable, `me` is assigned to point to the
new object. Only the `main()` frame is on the stack right now.
  ![A new `MemoryExample` object has been allocated on the heap and instantiated. The local variable, `me` is assigned to point to the new object. Only the `main()` frame is on the stack right now.](Memory-Line-by-line-02.png)

1. `main()` has now called the `useStrings()` method and its frame has been
created on the stack. It has no parameters, so the only variable
currently available on the stack is `this`, the reference to the
`MemoryExample` that `main()` called `useStrings()` on. `res1` and
`res2` are marked inaccessible because the lines of code declaring them
haven't executed yet
  ![`main()` has now called the `useStrings()` method and its frame has been created on the stack. It has no parameters, so the only variable currently available on the stack is `this`, the reference to the `MemoryExample` that `main()` called `useStrings()` on. `res1` and `res2` are marked inaccessible because the lines of code declaring them haven't executed yet](Memory-Line-by-line-03.png)

1. `useStrings()` has called the `concat()` method, passing in `"One:
"` and `1`. A new frame has been added for `concat()`, which has `this`
as well as the two method parameters, `s` and `i`. `this` is the object
`concat()` was called on (because `useStrings()` is an instance method
on the `MemoryExample` object, Java knows that `concat()` is called on
the same object without needing to call `this.concat("One: ", 1);` ,
though we could have written it that way instead). `s` is an object
reference, pointing to the `String` on the heap. `i` contains its value,
1, directly. The lower frames in the stack are inactive.
  ![`useStrings()` has called the `concat()` method, passing in `"One: "` and `1`. A new frame has been added for `concat()`, which has `this` as well as the two method parameters, `s` and `i`. `this` is the object `concat()` was called on (because `useStrings()` is an instance method on the `MemoryExample` object, Java knows that `concat()` is called on the same object without needing to call `this.concat("One: ", 1);` , though we could have written it that way instead). `s` is an object reference, pointing to the `String` on the heap. `i` contains its value, 1, directly. the lower frames in the stack are inactive.](Memory-Line-by-line-04.png)

1. `concat()` has created a new variable, `res` and assigned it to the
empty string on the heap.
  ![`concat()` has created a new variable, `res` and assigned it to the empty string on the heap.](Memory-Line-by-line-05.png)

1. Inside the `else` block, `res` has been re-assigned to a new `String`,
formed by concatenating the values of `s` and `i`. The old `String` that
`res` used to point to no longer has any references to it and is now
eligible for garbage collection.
  ![Inside the `else` block, `res` has been re-assigned to a new `String`, formed by concatenating the values of `s` and `i`. The old `String` that `res` used to point to no longer has any references to it and is now eligible for garbage collection.](Memory-Line-by-line-06.png)

1. `concat()` has returned and its frame has been popped off the stack.
`concat()`‘s return value (used to be pointed to by `res`) is now
pointed to by `res1`, the variable in the `useStrings()` frame. Nothing
on the stack now points to the "One: " `String` that was created in the
first call to `concat()` so it is now garbage collectable as well.
  ![`concat()` has returned and its frame has been popped off the stack. `concat()`‘s return value (used to be pointed to by `res`) is now pointed to by `res1`, the variable in the `useStrings()` frame. Nothing on the stack now points to the "One: " `String` that was created in the first call to `concat()` so it is now garbage collectable as well.](Memory-Line-by-line-07.png)

1. `useStrings()` has now called `concat()` , so a new `concat()` frame is
added. `this` and method arguments have been assigned.
  ![`useStrings()` has now called `concat()` , so a new `concat()` frame is added. `this` and method arguments have been assigned.](Memory-Line-by-line-08.png)

1. `res` is assigned empty string, and we're representing something like
what Java actually does, and pointing it back to the same empty string
instance that was used before. This has nothing to do with the fact that
it's the same local variable as before, and **only** represents that
Java reuses literal `String`s across methods and classes. If you assign
two different `String` variables equal to the same literal string (e.g.
"abc") the two references will be the same, and point to the exact same
`String` object.
  ![`res` is assigned empty string, and we're representing something like what Java actually does, and pointing it back to the same empty string instance that was used before. This has nothing to do with the fact that it's the same local variable as before, and just represents that Java reuses literal strings across methods and classes.](Memory-Line-by-line-09.png)

1. We've stepped into the `if` clause, because `i` is equal to 0. A new
variable, `add`, has been declared inside the `if` block and points to a
new `String` containing "zero". The `add` variable will only be
accessible inside the `if` block.
  ![We've stepped into the `if` clause, because `i` is equal to 0. A new variable, `add`, has been declared inside the `if` block and points to a new `String` containing "zero". The `add` variable will only be accessible inside the `if` block.](Memory-Line-by-line-10.png)

1. `res` has been reassigned to a new `String` formed by concatenating the
two strings. Its old value (the empty string) and the `String` pointed
to by `add` are both now garbage collectable because no stack variables
point to them anymore.
  ![`res` has been reassigned to a new `String` formed by concatenating the two strings. Its old value (the empty string) and the `String` pointed to by `add` are both now garbage collectable because no stack variables point to them anymore.](Memory-Line-by-line-11.png)

1. The return value of `concat()` has been assigned to `res2`, and the
stack frame for `concat()` has been popped off, so now the
`useStrings()` frame is active again.
  ![The return value of `concat()` has been assigned to `res2`, and the stack frame for `concat()` has been popped off, so now the `useStrings()` frame is active again.](Memory-Line-by-line-12.png)

1. `useStrings()` has returned, so its frame has been removed, and all of
the objects that its variables pointed to are now garbage collectable.
The only objects pointed to from variables on the stack now are `arg`,
the argument to `main()` and the local variable, `me` within `main()`.
After this, the program ends and all memory is eligible for garbage
collection (then likely freed completely as the JVM exits as well).
  ![`useStrings()` has returned, so its frame has been removed, and all of the objects that its variables pointed to are now garbage collectable. The only objects pointed to from variables on the stack now are `arg`, the argument to `main()` and the local variable, `me` within `main()`. After this, the program ends and all memory is eligible for garbage collection (then likely freed completely as the JVM exits as well).](Memory-Line-by-line-13.png)

----

(Optional) If you're really loving this and want an even more
complicated example of multiple method calls, try this
[sequence of stack/heap diagrams for a bank account](http://pages.cs.wisc.edu/~weinrich/cs302/handouts/BankAccountWalkthrough.pdf)
example.
