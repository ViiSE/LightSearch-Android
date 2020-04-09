package ru.viise.lightsearch.cmd.network.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;
import java.util.Objects;

import ru.viise.lightsearch.R;
import ru.viise.lightsearch.cmd.ClientCommands;
import ru.viise.lightsearch.cmd.network.NetworkService;
import ru.viise.lightsearch.cmd.process.Processes;
import ru.viise.lightsearch.cmd.process.ProcessesImpl;
import ru.viise.lightsearch.data.SearchFragmentContentEnum;
import ru.viise.lightsearch.data.entity.Command;
import ru.viise.lightsearch.data.entity.CommandResult;
import ru.viise.lightsearch.data.entity.SkladListCommandSimple;
import ru.viise.lightsearch.data.entity.SkladListCommandWithToken;
import ru.viise.lightsearch.data.entity.TKListCommandSimple;
import ru.viise.lightsearch.data.entity.TKListCommandWithToken;
import ru.viise.lightsearch.data.pojo.SendForm;
import ru.viise.lightsearch.data.pojo.SkladListPojo;
import ru.viise.lightsearch.data.pojo.SkladListPojoResult;
import ru.viise.lightsearch.data.pojo.TKListPojo;
import ru.viise.lightsearch.data.pojo.TKListPojoResult;
import ru.viise.lightsearch.fragment.SpinnerWithCallback;
import ru.viise.lightsearch.pref.PreferencesManager;
import ru.viise.lightsearch.pref.PreferencesManagerInit;
import ru.viise.lightsearch.pref.PreferencesManagerType;

public class FillAdapterAsyncTask extends AsyncTask<String[], Void, String[]> {

    private final WeakReference<SpinnerWithCallback> wrSpinner;
    private final WeakReference<ProgressBar> wrProgressBar;
    private final WeakReference<Activity> wrActivity;
    private final String commandName;

    public FillAdapterAsyncTask(
            WeakReference<SpinnerWithCallback> wrSpinner,
            WeakReference<ProgressBar> wrProgressBar,
            WeakReference<Activity> wrActivity,
            String commandName) {
        this.wrSpinner = wrSpinner;
        this.wrProgressBar = wrProgressBar;
        this.wrActivity = wrActivity;
        this.commandName = commandName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        wrProgressBar.get().setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected String[] doInBackground(String[]... dataArrays) {
        String[] dataArray = dataArrays[0];

        if(dataArray.length == 0) {
            SharedPreferences sPref = wrActivity.get().getSharedPreferences("pref", Context.MODE_PRIVATE);
            PreferencesManager prefManager = PreferencesManagerInit.preferencesManager(sPref);

            Command<? extends SendForm> command;
            if(commandName.equals(ClientCommands.SKLAD_LIST)) {
                command = new SkladListCommandWithToken(
                        new SkladListCommandSimple(),
                        prefManager.load(PreferencesManagerType.TOKEN_MANAGER));
            } else {
                command = new TKListCommandWithToken(
                        new TKListCommandSimple(),
                        prefManager.load(PreferencesManagerType.TOKEN_MANAGER));
            }

            Processes processes = new ProcessesImpl(NetworkService.getInstance());
            String[] recs = new String[0];
            if(command.name().equals(ClientCommands.SKLAD_LIST)) {
                CommandResult<SkladListPojo, SkladListPojoResult> result = processes.process(command.name()).apply((Command) command);
                recs = result.data().getSkladList();
            } else if(command.name().equals(ClientCommands.TK_LIST)) {
                CommandResult<TKListPojo, TKListPojoResult> result = processes.process(command.name()).apply((Command) command);
                recs = result.data().getTKList();
            }

            return result(recs);
        } else
            if(dataArray[0].equals(SearchFragmentContentEnum.ALL_UI.stringValue()))
                return dataArray;
            else
                return result(dataArray);
    }

    private String[] result(String[] dArr) {
        String[] data = new String[dArr.length + 1];
        data[0] = SearchFragmentContentEnum.ALL_UI.stringValue();
        System.arraycopy(dArr, 0, data, 1, dArr.length);

        return data;
    }

    @Override
    protected void onPostExecute(String[] recs) {
        super.onPostExecute(recs);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                Objects.requireNonNull(wrActivity.get()),
                R.layout.support_simple_spinner_dropdown_item,
                recs);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        wrSpinner.get().setAdapter(adapter);
        wrSpinner.get().call(recs);
        wrProgressBar.get().setVisibility(View.GONE);
    }
}
