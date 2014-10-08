#include <stdio.h>
#include <time.h>
#include <signal.h>
#include <sched.h>

volatile int looping = 1;

struct sigaction sa;
struct itimerval timer;

void timer_callback(int sig)
{
    looping = 0;
    printf(" Catched timer signal: %d\n", sig);

}

void start_timer(int interval)
{

    /* Install timer_callback as the signal handler for SIGVTALRM. */
    memset (&sa, 0, sizeof (sa));
    sa.sa_handler = &timer_callback;
    sigaction (SIGVTALRM, &sa, NULL);

    /* Configure the timer to expire after C*/
    timer.it_value.tv_sec = 0;
    timer.it_value.tv_usec = C;
    /* ... and every 250 msec after that. */
    timer.it_interval.tv_sec = 0;
    timer.it_interval.tv_usec = 0;

    setitimer(ITIMER_VIRTUAL, &timer, NULL);

}

// void stop_timer(void)
// {


// 	struct itimerspec value;

//     timer.it_value.tv_sec = 0;
//     timer.it_value.tv_usec = 0;

//     timer.it_interval.tv_sec = 0;
//     timer.it_interval.tv_usec = 0;

// 	timer_settime (gTimerid, 0, &value, NULL);


// }

int main(int argc, char **argv)
{
    int C, T, cpuID;

    if (argc != 4){
        printf("Invalid argument\n");
        return 1;
    }

    C = atoi(argv[1]) * 1000;
    T = atoi(argv[2]) * 1000;
    cpuID = atoi(argv[3]);

    if (cpuID < 0 || cpuID > 3){
        printf("Invalid cpuID %d\n", cpuID);
        return 1;
    }

    if (C > T || C < 0 || T < 0 ||  T > 60000000){
    	printf(""Invalid timer interval %d %d"\n", C, T);
    }


    cpu_set_t  mask;
    CPU_ZERO(&mask);
    CPU_SET(cpuID, &mask);
    if(sched_setaffinity(0, sizeof(mask), &mask) < 0){
    	printf("set cpu affinity failed\n");
    	return -1;
    }


    while (1)
    {
        looping = 1;
        start_timer(C);

        while (looping)
        {continue;}

        printf("here\n");

        usleep(T - C);

    }

}