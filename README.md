JYCAndroidLogger
======
Are you looking for a logger can modify upload process by yourself? well today is your lucky day.  

> **warning: JYCAndroidLogger is base on [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)**
>
> JYCAndroidLogger is base on WorkManager, so some device(most of them is chinese phone) won't work correctly, you can check [Don'tKillMyApp](https://dontkillmyapp.com/)

Last Version
--------
```
1.0.0
```

usage
---------
add
```groovy
implementation "io.github.chinacoolder:jycandroidlogger:${last_version}"
```
then create `Upload` class in your project
```kotlin
class Upload: IUpload {
    override fun upload(ctx: Context, logs: List<LogEntity>): Boolean {
        //upload log
        return true
    }
}
```
init in your application `onCreate`
```kotlin
JYCLogger.Builder(applicationContext, Upload::class.java)
    .build()
```
everything is ready now, you can log in your code and these log will invoke `Upload.upload`
```kotlin
JYCLogger.INSTANCE.log("test", "test")
```

AddPolicy
======
you can add upload policy to decide is log should be upload  
now support 2 Policy `NumberPolicy` and `TimePolicy`, you can add them when you init the logger
```kotlin
JYCLogger.Builder(applicationContext, Upload::class.java)
    .policy(
        NumberPolicy(100) // this means log will upload when count is larger than 100
    )
    .build()
```
```kotlin
JYCLogger.Builder(applicationContext, Upload::class.java)
    .policy(
        TimePolicy.hours(1)// this means The log upload interval should not be less than 1 hour
    )
    .build()

JYCLogger.Builder(applicationContext, Upload::class.java)
    .policy(
        TimePolicy.hours(1)// this means The log upload interval should not be less than 1 day
    )
    .build()

JYCLogger.Builder(applicationContext, Upload::class.java)
    .policy(
        TimePolicy.minutes(30)// this means The log upload interval should not be less than 30 minutes
    )
    .build()
```

CustomPolicy
======
you can define your own policy to decide is log should be upload
```kotlin
class Policy: IPolicy {
    override fun pass(count: Int, lastTimeUpload: Long): Boolean {
        //count is the log count having now
        //lastTimeUpload is the timestamp when last time upload log
        //return true if policy is passed, otherwise return false
        return true
    }
}
```

CustomSaver
======
the default log save is `PrivateStorageSaver`, this will store log in your [app-specific](https://developer.android.com/training/data-storage/app-specific)  
and of course, you can define your custom saver
```kotlin
class Saver: ISave {
    override fun save(context: Context, entity: LogEntity) {
        //save the log to specific place
    }

    override fun provide(context: Context): List<LogEntity> {
        //return the log list currently store, return empty list if don't have any log
    }

    override fun provideCount(context: Context): Int {
        //return the log count currently store, return 0 if don't have any log
    }

    override fun updateUploadTime(context: Context, timeStamp: Long) {
        //upload log is success, update the upload timestamp
    }

    override fun provideUploadTime(context: Context): Long {
        //provide the timestamp of last upload, return 0 if log not uploaded
    }

    override fun clear(context: Context) {
        //clear the log
    }
}
```

License
-------

```
Copyright JiaYiChi.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```