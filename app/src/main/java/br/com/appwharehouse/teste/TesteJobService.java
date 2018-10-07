package br.com.appwharehouse.teste;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class TesteJobService extends JobService {
//    private JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i("Teste", "onStartJob()");
//        this.jobParameters = jobParameters;

        Repository repository = Repository.getInstance(); // Singleton class, aqui já deve estar instanciada
        repository.getTicker(getApplication());

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("Teste", "onStopJob()");
        return true;
    }

}
