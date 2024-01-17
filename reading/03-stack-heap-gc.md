# The Stack, Heap, and Garbage Collection

As you learned in the previous reading, memory is divided into the stack 
and the heap. The stack stores all of the variables tied to a specific method. 
The heap is a much larger portion of memory that stores the objects that the 
application is using. In this reading, we'll look at another coding example 
to see how data is stored in the stack and heap, and learn about how the JVM 
reclaims unused memory.

### The Stack and Heap

Recall that the **stack** is the portion of memory reserved for local variables in
methods currently executing. It contains groups of data called frames. 
Frames contain data such as:
* a reference to the object whose method is being executed (the `this`
  reference)
* parameters passed into the method
* local variables declared in the method

These frames "stack" on top of one another, so that a new frame is added
to the "top" when each new method is called, and a frame is removed from
the "top" when that method returns. Only the frame that's currently on
"top" is accessible/active at any point in time. 

The **heap** is the portion of memory reserved for storing objects'
state: their member variables. Remember that object references/variables
are memory addresses that point to the locations in memory where the
objects are stored.

The data inside an object in memory are the member variables of that
object (the variables declared at the top of the class):
* The *primitive* member variables' values are stored right inside the
  object's location in the heap.
* The *object* member variables' references/addresses are stored inside
  the object's location in memory. These in turn point to other objects
  somewhere else on the heap.

The size of memory allocated to an object depends on the member
variables declared in its class. No other objects can occupy the same
chunk of memory "allocated" to that object (until that object's memory
is freed--more on this later). The JVM keeps track of which chunks of
memory are occupied/allocated by every object running in the JVM.

Here's some example code with the corresponding memory diagram
as the program is running. The code execution began in `main()` at the
bottom, which called `useStrings()`, which has called `concat()`. The
lines of code that have executed/are being executed are in `**bold**`.
The line of code to be executed next is highlighted in light green
(`return res;`). On the bottom of the stack is the frame for `main()`.
On top of this we have the method that `main()` is calling,
`useStrings()`. The top of the stack has the method that `useStrings()`
is currently calling, `concat()`. The grey arrows show where in the code
each stack variable comes from: `s` and `i` are method arguments, and
`res` is a local variable, most recently updated inside the `else`
block.

![Stack/heap diagram of memory example code](Memory-Stack-Plus-Heap.png)

The highlighted line of code shows where the program is (and bold code
has already been called). `main()` has called `useStrings()` on the
`MemoryExample` object, `me`, which in turn called `concat()`, also on
the same `MemoryExample` object, passing in `"One: "` and `1` as the
arguments.

There are currently three frames on the stack—one for each method,
starting with `main()`—showing the variables within each frame. Only the
top-most frame for `concat()` is currently active. The `useStrings()`
and `main()` frames are inactive right now (shaded background). This
means that `me` and `res1` can't be referenced in the current method,
for example.

We're representing variables as two boxes: one with the name of the
variable, and another with the value, which is either a primitive value
or a reference. A primitive has the value (e.g. 1). A reference has a
blue dot with an arrow pointing to the object in the heap that its
reference points to.

Inside the `concat()` frame, we have space reserved for five variables:

* `this`: the reference to the object that `concat()` was called on in
  `useStrings()`. As a result, the `this` variables in both
  `useStrings()` and `concat()` point to the same object in the heap that `me`
  points to in the `main()` method.
* `s` is the first argument, and since it is a `String` type, it
  contains a reference to the `String` object ("One: ") passed in by
  `useStrings()`.
* `i` is the second argument, and since it is an `int` (a primitive), it
  contains the value itself, 1.
* `add` is not currently in scope (the code is not executed inside the
  `if` block right now), so is not usable in the code.


## What is Scope? ("in scope" / "out of scope" / "scoping")

A variable is "in **scope**" when it can be referenced in the current
section of code. We have learned about this when trying to get our code
to compile. You need to declare your method's local variables before the
code that uses them, or else you get a compile error. Local variables
declared within a method can be used anywhere after the declaring line
within that method. Local variables declared within an `if-`block or a
`for-`loop can only be used within those specific blocks. The Java
compiler won't let us refer to a variable that is "out of scope". The
variables that are currently in scope are the set of variables that live
in the currently active frame, which includes:

* `this`
* The method's parameters
* Any local variables inside that method. Note that you can declare
  variables within an `if`/ `for`/ `while` block, which are only in
  scope within that specific block. You can think of these as being
  added/removed to the current frame as you enter/leave the code block

In the diagram above, the `concat()` stack frame is active and thus its
variables are in scope. The `add` variable has space allocated to it on
the stack, but it's only in scope when inside the `if`-block, so in this
diagram `add` is currently out of scope and not accessible. Everything
in the `useStrings()` frame is out of scope, as that method has the
`concat()` frame on top of it and is not currently active.

## Why doesn't the JVM run out of (heap) memory all the time? (Garbage collection)

We know the stack frees up space whenever a method completes and the old
frame is popped off the top of the stack. Consider a program that
creates a new object in the heap and keeps track of its reference in a
variable. Imagine this program creates another object in the heap and
reassigns the variable to the new object's reference. The original
object is no longer referenced anywhere in the program, but it still
occupies space in the heap; it's "abandoned". A program that did this
often enough could eventually run out of heap space for new objects!
Admittedly, the heap is typically huge, and the program would have to
make a lot of objects to do this, but it could eventually happen. So the
JVM has got to "reclaim" this memory used by "abandoned" objects.

The JVM could update its internal state, "deallocating" memory for each
object that is no longer used, but it's kind of hard to tell by looking
at the heap what is or isn't "used" right now. The key is that since the
stack is where all of the variables are that are available to the
executing methods, only objects that can be reached somehow by following
variables on the stack need to be kept around. However, figuring this
out means following all of the stack's variables' references, their
objects' references etc., so the JVM isn't doing this every time a
method completes. It waits to do this expensive computation every once
in a while instead of interrupting your program all the time—you'd never
get any work done otherwise!

An alternative could be for the person writing the code (you!) to
explicitly request and free memory when you need it, then store your
objects' data in the space that you have requested. The C programming
language works this way, for example, and it's a near endless source
of pain and suffering. The designers of Java have decided to avoid this.

Enter **garbage collection (GC)**, which is the way that Java figures
out which objects are still in use, then reclaims memory from the heap
when objects are no longer used by the Java program. Garbage collection
runs periodically, interrupting your program's execution to identify
objects that can have their locations in memory reclaimed. The garbage
collector figures out which objects on the heap no longer have a
reference pointing to them directly or indirectly via any variables on
the stack. If any stack variable is a direct reference to the object on
the heap, or a stack variable is a reference to an object that has a
reference to the object on the heap (or has a reference... etc.), then
that object is not currently eligible for garbage collection, and the
garbage collector leaves it alone. GC will identify any such eligible
objects, then free their memory before returning execution to your Java
program. When GC is done, the JVM goes back to exactly where your
program was executing (with exactly the same stack) and starts chugging
along again.

This diagram shows a program in progress, with some of the objects in
the heap shaded in grey, indicating that they have no references from
the stack, and are thus eligible for garbage collection. You can ignore
the code if you like, just look at the objects that are pointed to by
the variables on the stack. The objects on the heap that are eligible
for GC are shaded in grey. Several objects are *not *eligible for
garbage collection (no shading). The unshaded `Strings` are pointed to
directly by variables on the stack; the `char[]` are pointed to by the
`String` objects.

![Several objects on the heap are no longer pointed to by variables on the stack, so they are eligible for garbage collection](Memory-Garbage-Collectable.png)

For example, the `String` and its `char[]` that contained "zero" do not
have any paths pointing to them from anywhere on the stack. Those two
shaded objects are eligible for garbage collection. After the GC runs,
these two objects might be deallocated and removed from the heap, their
bytes made available to be occupied by future objects. The empty
`String` and the "One: " `String` and their `char[]` objects are all
eligible for garbage collection as well, for the same reason.

On the other hand, the `String` pointed to by `res1` is not garbage
collectable (because `res1` points to it), even though `res1` is not
currently in scope (it lives in the second frame on the stack from the
method calling the current method).
