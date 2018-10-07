package br.com.appwharehouse.teste;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class TesteJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i("Teste", "onStartJob()");

        Repository repository = Repository.getInstance(); // Singleton class, aqui já deve estar instanciada
        repository.getTicker(getApplication());

        jobFinished(jobParameters, false);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("Teste", "onStopJob()");
        return true;
    }

}
