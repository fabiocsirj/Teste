package br.com.appwharehouse.teste;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public abstract class TesteJogScheduler {

        public static void start(Context context) {
        ComponentName componentName = new ComponentName(context, TesteJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(80, componentName)
                .setPeriodic(15 * 60 * 1000)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        assert jobScheduler != null;
        int resultCode = jobScheduler.schedule(jobInfo);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.i("Teste", "Job agendado...");
        } else {
            Log.i("Teste", "Agendamento do Job FALHOU!");
        }
    }

}
