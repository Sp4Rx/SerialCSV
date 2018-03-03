package io.github.sp4rx.serialcsvlibarary;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by suvajit on 28/2/18.
 * This class is used to set-up the csv export operation.
 */

public class SerialCSV<T> {
    private File file;
    private Listener listener;
    private String[] headings;

    /**
     * Listener for onSuccess and onFailure event
     */
    public interface Listener {
        /**
         * On success it returns the generated csv file
         *
         * @param file Generated csv file
         */
        void onSuccess(File file);

        /**
         * On failure it returns the failure message
         *
         * @param message Failure message
         */
        void onFailure(String message);
    }

    /**
     * Exports a List of object to csv.
     *
     * @param data  {{@link List}} of {{@link Object}}
     * @param clazz {{@link Class}} of the {{@link Object}}
     */
    public void export(List<T> data, Class clazz) {
        new Exporter<>(data, clazz, this.headings, this.file, this.listener).execute();
    }

    /**
     * Returns success or failure callback.
     *
     * @param listener Instance of {{@link Listener}}
     * @return {{@link SerialCSV}}
     */
    public SerialCSV<T> setCallback(Listener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Set file if you want to save it in custom location
     *
     * @param file {{@link File}} with custom location
     * @return {{@link SerialCSV}}
     */
    public SerialCSV<T> setFile(File file) {
        this.file = file;
        return this;
    }

    /**
     * Set custom headings
     *
     * @param headings String array
     * @return {{@link SerialCSV}}
     */
    public SerialCSV<T> setHeading(String... headings) {
        Arrays.sort(headings, String.CASE_INSENSITIVE_ORDER);
        this.headings = headings;
        return this;
    }

    /**
     * Set custom headings
     *
     * @param headings List of headings
     * @return {{@link SerialCSV}}
     */
    public SerialCSV<T> setHeading(List<String> headings) {
        Collections.sort(headings, String.CASE_INSENSITIVE_ORDER);
        this.headings = headings.toArray(new String[0]);
        return this;
    }
}
