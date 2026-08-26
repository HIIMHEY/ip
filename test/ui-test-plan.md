# Tasque Console UI Test Plan

This file records persistent console regression tests. Expected-output blocks
contain Tasque process stdout only; input echo is shown separately by the test
runner.

## TC-L3-01: Add, list, mark, and unmark a task

**Aim:** Preserve the completed Level-0 to Level-3 console workflow.

### Inputs

```console-input
todo read book
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
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list
Here are the tasks in your list:
1.[T][ ] read book
Nice! I've marked this task as done:
[T][X] read book
OK, I've marked this task as not done yet:
[T][ ] read book

Goodbye! See you again soon.
```

## TC-L7-01: Persist tasks across restart

**Aim:** Verify that Todo, Deadline, and Event tasks and their details are restored after restarting Tasque.

### Inputs

```console-input
todo read book
deadline submit report /by 31 Dec
event team meeting /from Monday 2pm /to Monday 4pm
bye
--- RESTART ---
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
[T][ ] read book
Now you have 1 tasks in the list
Got it. I've added this task:
[D][ ] submit report (by: 31 Dec)
Now you have 2 tasks in the list
Got it. I've added this task:
[E][ ] team meeting (from: Monday 2pm to: Monday 4pm)
Now you have 3 tasks in the list

Goodbye! See you again soon.
========================================
                 TASQUE                 
========================================

Hello! I'm Tasque.
What can I do for you?
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] submit report (by: 31 Dec)
3.[E][ ] team meeting (from: Monday 2pm to: Monday 4pm)

Goodbye! See you again soon.
```

## TC-L5-01: Reject empty Todo descriptions

**Aim:** Reject missing or blank Todo descriptions without terminating Tasque or adding a task.

### Inputs

```console-input
todo
todo   
todo read book
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
OOPS!!! The description of a todo cannot be empty.
OOPS!!! The description of a todo cannot be empty.
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list
Here are the tasks in your list:
1.[T][ ] read book

Goodbye! See you again soon.
```

## TC-L5-02: Reject incomplete Deadlines

**Aim:** Reject Deadlines with missing descriptions, missing `/by`, or empty `/by` values.

### Inputs

```console-input
deadline
deadline /by Sunday
deadline return book
deadline return book /by
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
OOPS!!! The description of a deadline cannot be empty.
OOPS!!! The description of a deadline cannot be empty.
OOPS!!! A deadline must include /by followed by when it is due.
OOPS!!! The /by value of a deadline cannot be empty.
Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 1 tasks in the list
Here are the tasks in your list:
1.[D][ ] return book (by: Sunday)

Goodbye! See you again soon.
```

## TC-L5-03: Reject incomplete Events

**Aim:** Reject Events with missing descriptions, missing delimiters, or empty start/end values.

### Inputs

```console-input
event
event /from Mon 2pm /to 4pm
event project meeting /to 4pm
event project meeting /from Mon 2pm
event project meeting /from /to 4pm
event project meeting /from Mon 2pm /to
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
OOPS!!! The description of an event cannot be empty.
OOPS!!! The description of an event cannot be empty.
OOPS!!! An event must include /from followed by its start.
OOPS!!! An event must include /to followed by its end.
OOPS!!! The /from value of an event cannot be empty.
OOPS!!! The /to value of an event cannot be empty.
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 1 tasks in the list
Here are the tasks in your list:
1.[E][ ] project meeting (from: Mon 2pm to: 4pm)

Goodbye! See you again soon.
```

## TC-L5-04: Reject invalid commands and task numbers

**Aim:** Reject unknown commands and invalid mark/unmark task numbers without changing tasks or terminating Tasque.

### Inputs

```console-input
blah
mark
unmark
mark abc
unmark xyz
todo read book
mark 0
unmark -1
mark 2
unmark 2
mark 1
unmark 1
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
OOPS!!! I do not recognize that command.
OOPS!!! The mark command needs a task number.
OOPS!!! The unmark command needs a task number.
OOPS!!! The task number must be a positive whole number.
OOPS!!! The task number must be a positive whole number.
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list
OOPS!!! The task number must be a positive whole number.
OOPS!!! The task number must be a positive whole number.
OOPS!!! Task 2 does not exist in the list.
OOPS!!! Task 2 does not exist in the list.
Nice! I've marked this task as done:
[T][X] read book
OK, I've marked this task as not done yet:
[T][ ] read book
Here are the tasks in your list:
1.[T][ ] read book

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

## TC-L6-01: Delete the only task

**Aim:** Delete the only task, report the removed task, and leave an empty task list.

### Inputs

```console-input
todo read book
delete 1
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
[T][ ] read book
Now you have 1 tasks in the list
Noted. I've removed this task:
[T][ ] read book
Now you have 0 tasks in the list
Here are the tasks in your list:

Goodbye! See you again soon.
```

## TC-L6-02: Delete first and middle tasks

**Aim:** Preserve order, renumber remaining tasks, and support mark/unmark after deletion.

### Inputs

```console-input
todo first task
deadline second task /by Sunday
event third task /from Mon 2pm /to 4pm
todo fourth task
delete 1
list
delete 2
list
mark 2
unmark 2
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
[T][ ] first task
Now you have 1 tasks in the list
Got it. I've added this task:
[D][ ] second task (by: Sunday)
Now you have 2 tasks in the list
Got it. I've added this task:
[E][ ] third task (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list
Got it. I've added this task:
[T][ ] fourth task
Now you have 4 tasks in the list
Noted. I've removed this task:
[T][ ] first task
Now you have 3 tasks in the list
Here are the tasks in your list:
1.[D][ ] second task (by: Sunday)
2.[E][ ] third task (from: Mon 2pm to: 4pm)
3.[T][ ] fourth task
Noted. I've removed this task:
[E][ ] third task (from: Mon 2pm to: 4pm)
Now you have 2 tasks in the list
Here are the tasks in your list:
1.[D][ ] second task (by: Sunday)
2.[T][ ] fourth task
Nice! I've marked this task as done:
[T][X] fourth task
OK, I've marked this task as not done yet:
[T][ ] fourth task
Here are the tasks in your list:
1.[D][ ] second task (by: Sunday)
2.[T][ ] fourth task

Goodbye! See you again soon.
```

## TC-L6-03: Reject invalid delete commands

**Aim:** Reject invalid delete task numbers without modifying tasks or terminating Tasque.

### Inputs

```console-input
delete
delete abc
delete 0
delete -1
delete 1
todo read book
delete 2
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
OOPS!!! The delete command needs a task number.
OOPS!!! The task number must be a positive whole number.
OOPS!!! The task number must be a positive whole number.
OOPS!!! The task number must be a positive whole number.
OOPS!!! Task 1 does not exist in the list.
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list
OOPS!!! Task 2 does not exist in the list.
Here are the tasks in your list:
1.[T][ ] read book

Goodbye! See you again soon.
```
