[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=EssDeeBee_upsert-generator&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=EssDeeBee_upsert-generator)
# upsert-generator
Generate effective upsert SQL scripts for fast inserting to a DB

Tests show 30 times execution speed improvement:

```
____________Run #1______________
Saving using upsert:
    Time: 1370 ms
    Students Processed: 59882
Saving using repository:
    Time: 44838 ms
    Students Processed: 59882
    
Upsert is 32 times faster
____________Run #1______________

____________Run #2______________
Saving using upsert:
    Time: 1622 ms
    Students Processed: 81924
Saving using repository:
    Time: 60089 ms
    Students Processed: 81924
    
Upsert is 37 times faster
____________Run #2______________

____________Run #3______________
Saving using upsert:
    Time: 1918 ms
    Students Processed: 95060
Saving using repository:
    Time: 68984 ms
    Students Processed: 95060
    
Upsert is 35 times faster
____________Run #3______________

____________Run #4______________
Saving using upsert:
    Time: 318 ms
    Students Processed: 15988
Saving using repository:
    Time: 11615 ms
    Students Processed: 15988
    
Upsert is 36 times faster
____________Run #4______________

____________Run #5______________
Saving using upsert:
    Time: 966 ms
    Students Processed: 50898
Saving using repository:
    Time: 36791 ms
    Students Processed: 50898
    
Upsert is 38 times faster
____________Run #5______________

Upsert is 35 times faster on average
```

