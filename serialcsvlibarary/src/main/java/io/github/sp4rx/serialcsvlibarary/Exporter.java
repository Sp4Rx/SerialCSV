package io.github.sp4rx.serialcsvlibarary;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by suvajit on 28/2/18.<br>
 * This is the main class which handles the export operation , which is extended from {{@link AsyncTask}}.
 */

class Exporter<T> extends AsyncTask<Void, Void, File> {
    private static final String TAG = "Exporter";
    public static final String APP_NAME = "SerialCSV";
    public static final String EXTENSION = ".csv";
    private final List<T> data;
    private final Class clazz;
    private String[] headings;
    private File file;
    private SerialCSV.Listener listener;
    private String errorMessage;

    /**
     * Default constructor
     *
     * @param data     {{@link List}} of {{@link Object}}
     * @param clazz    {{@link Class}} of the {{@link Object}}
     * @param headings
     * @param file     {{@link File}} with custom location
     * @param listener Instance of {{@link SerialCSV.Listener}}
     */
    Exporter(List<T> data, Class clazz, String[] headings, @Nullable File file, @Nullable SerialCSV.Listener listener) {
        this.data = data;
        this.clazz = clazz;
        this.headings = headings;
        this.file = file;
        this.listener = listener;
    }

    @Override
    protected File doInBackground(Void... voids) {
        Field[] tempFields = clazz.getFields();
        ArrayList<Field> fields = new ArrayList<>();
        File file = null;

        /*Remove some default fields*/
        for (Field field : tempFields) {
            if (!field.getName().equals("$change") && !field.getName().equals("serialVersionUID"))
                fields.add(field);
        }
        StringBuilder stringBuilder = new StringBuilder();

        /*Add heading row to StringBuilder for the csv*/
        if (headings == null) {
            for (Field field : fields) {
                stringBuilder.append(field.getName()).append(",");
            }
        } else {
            if (fields.size() != headings.length) {
                errorMessage = "Invalid heading count. It must be " + fields.size();
                return null;
            } else {
                if (!isDuplicateHeading()) {
                    for (String heading : headings) {
                        stringBuilder.append(heading).append(",");
                    }
                } else {
                    errorMessage = "Heading contains duplicate values.";
                    return null;
                }
            }
        }

        /*Add data from the list to StringBuilder for the csv */
        if (!data.isEmpty()) {
            for (T obj : data) {
                if (obj != null) {
                    stringBuilder.append("\n");
                    for (Field field : fields) {
                        try {
                            Object tempObj = field.get(obj);
                            if (tempObj == null) {
                                stringBuilder.append("").append(",");
                            } else {
                                stringBuilder.append(field.get(obj)).append(",");
                            }
                        } catch (IllegalAccessException e) {
                            if (BuildConfig.DEBUG)
                                e.printStackTrace();
                            errorMessage = e.getMessage();
                            return null;
                        }
                    }
                }
            }

            /*Current date for the name of the file to save*/
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String currentDate = df.format(c.getTime());

            String fileName = APP_NAME + "-" + currentDate;

            /*Write to file*/
            File root = Environment.getExternalStorageDirectory();
            if (root.canWrite()) {
                if (this.file == null) {
                    File dir = new File(root.getAbsolutePath() + "/" + APP_NAME);
                    Log.d(TAG, "Dir path: " + dir.toString());
                    dir.mkdirs();
                    int suffix = 1;
                    file = new File(dir, fileName + "-" + (suffix) + EXTENSION);
                    while (file.exists()) {
                        file = new File(dir, fileName + "-" + (++suffix) + EXTENSION);
                    }
                } else {
                    file = this.file;
                }
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(stringBuilder.toString().getBytes());
                    out.close();
                } catch (IOException | NullPointerException e) {
                    if (BuildConfig.DEBUG)
                        e.printStackTrace();
                    errorMessage = e.getLocalizedMessage();
                    return null;
                }
                Log.d(TAG, "Data exported to: " + file.toString());

            } else {
                errorMessage = "No write permission";
                return null;
            }
        } else {
            errorMessage = "Data is empty";
            return null;
        }
        return file;
    }

    private boolean isDuplicateHeading() {
        Set<String> set = new HashSet<>();
        boolean status = false;
        for (String heading : headings) {
            if (!set.add(heading)) {
                status = true;
            }
        }
        return status;
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        /*Return callback*/
        if (listener != null) {
            if (file != null) {
                listener.onSuccess(file);
            } else {
                listener.onFailure(errorMessage);
            }
        }
    }
}
