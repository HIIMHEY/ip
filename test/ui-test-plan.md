# Tasque Console UI Test Plan

This file records persistent console regression tests. Expected-output blocks
contain Tasque process stdout only; input echo is shown separately by the test
runner.

## TC-L3-01: Add, list, mark, and unmark a task

**Aim:** Preserve the completed Level-0 to Level-3 console workflow.

### Inputs

```console-input
read book
list
mark 1
unmark 1
bye
```

### Expected output

```console-output
========================================
                 TASQUE                 
========================================

Hello! I'm Tasque.
What can I do for you?
added: read book
Here are the tasks in your list:
1.[ ] read book
Nice! I've marked this task as done:
[X] read book
OK, I've marked this task as not done yet:
[ ] read book

Goodbye! See you again soon.
```

## TC-L4-01: Create and list a Todo

**Aim:** Verify Todo creation and the `[T]` type marker in list output.

### Inputs

```console-input
todo borrow book
list
bye
```

### Expected output

```console-output
========================================
                 TASQUE                 
========================================

Hello! I'm Tasque.
What can I do for you?
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list
Here are the tasks in your list:
1.[T][ ] borrow book

Goodbye! See you again soon.
```

## TC-L4-02: Create and list a Deadline

**Aim:** Verify Deadline creation, the `[D]` type marker, and `/by` information.

### Inputs

```console-input
deadline return book /by Sunday
list
bye
```

### Expected output

```console-output
========================================
                 TASQUE                 
========================================

Hello! I'm Tasque.
What can I do for you?
Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 1 tasks in the list
Here are the tasks in your list:
1.[D][ ] return book (by: Sunday)

Goodbye! See you again soon.
```

## TC-L4-03: Create and list an Event

**Aim:** Verify Event creation, the `[E]` type marker, and `/from` and `/to` information.

### Inputs

```console-input
event project meeting /from Mon 2pm /to 4pm
list
bye
```

### Expected output

```console-output
========================================
                 TASQUE                 
========================================

Hello! I'm Tasque.
What can I do for you?
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 1 tasks in the list
Here are the tasks in your list:
1.[E][ ] project meeting (from: Mon 2pm to: 4pm)

Goodbye! See you again soon.
```

## TC-L4-04: Mark and unmark a mixed task list

**Aim:** Verify polymorphic list display and mark/unmark behavior for Todo, Deadline, and Event.

### Inputs

```console-input
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
mark 1
mark 2
mark 3
list
unmark 1
unmark 2
unmark 3
list
bye
```

### Expected output

```console-output
========================================
                 TASQUE                 
========================================

Hello! I'm Tasque.
What can I do for you?
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list
Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 2 tasks in the list
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list
Nice! I've marked this task as done:
[T][X] borrow book
Nice! I've marked this task as done:
[D][X] return book (by: Sunday)
Nice! I've marked this task as done:
[E][X] project meeting (from: Mon 2pm to: 4pm)
Here are the tasks in your list:
1.[T][X] borrow book
2.[D][X] return book (by: Sunday)
3.[E][X] project meeting (from: Mon 2pm to: 4pm)
OK, I've marked this task as not done yet:
[T][ ] borrow book
OK, I've marked this task as not done yet:
[D][ ] return book (by: Sunday)
OK, I've marked this task as not done yet:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)

Goodbye! See you again soon.
```
