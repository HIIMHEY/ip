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
