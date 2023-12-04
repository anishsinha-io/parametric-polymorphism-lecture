#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

// An int stack frame contains a data field of type integer and a pointer to the
// next stack frame
typedef struct int_stack_frame {
    int                     data;
    struct int_stack_frame* next;
} int_stack_frame;

// Constructor for
int_stack_frame* new_int_stack_frame(int data) {
    int_stack_frame* sf = malloc(sizeof(int_stack_frame));
    sf->data            = data;
    sf->next            = NULL;
    return sf;
}

typedef struct int_stack {
    struct int_stack_frame* top;
    int                     num_frames;
    int                     capacity;
} int_stack;

int_stack* new_int_stack(int capacity) {
    int_stack* s  = malloc(sizeof(int_stack));
    s->capacity   = capacity;
    s->num_frames = 0;
    s->top        = NULL;
    return s;
}

int push(int_stack* s, int data) {
    if (s->num_frames == s->capacity) {
        return -1;
    }
    int_stack_frame* sf = new_int_stack_frame(data);
    sf->next            = s->top;
    s->top              = sf;
    s->num_frames++;
    return 0;
}

int pop(int_stack* s, int* buf) {
    if (s->num_frames == 0) {
        return -1;
    }
    int_stack_frame* popped = s->top;
    *buf                    = popped->data;
    s->top                  = s->top->next;
    s->num_frames--;
    popped->next = NULL;
    free(popped->next);
    free(popped);
    return 0;
}

int main(int argc, char** argv) {
    int        status;
    int_stack* s1 = new_int_stack(3);

    status = push(s1, 5);
    assert(status == 0);

    status = push(s1, 6);
    assert(status == 0);

    status = push(s1, 7);
    assert(status == 0);

    status = push(s1, 8);
    assert(status != 0);

    int* buf = malloc(sizeof(int));

    status = pop(s1, buf);
    assert(status == 0);
    assert(*buf == 7);

    status = pop(s1, buf);
    assert(status == 0);
    assert(*buf == 6);

    status = pop(s1, buf);
    assert(status == 0);
    assert(*buf == 5);

    status = pop(s1, buf);
    assert(status != 0);
    assert(*buf == 5);

    return 0;
}
