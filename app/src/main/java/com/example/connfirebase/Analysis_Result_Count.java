package com.example.connfirebase;

import java.util.ArrayList;

public class Analysis_Result_Count {
    public Float[] count(String result, Float result_cnt[]) {

        if (result.equals("A+") || result.equals("A") || result.equals("A-")) {
            result_cnt[0] = result_cnt[0] + 1;
        } else if (result.equals("B+") || result.equals("B") || result.equals("B-")) {
            result_cnt[1] = result_cnt[1] + 1;
        } else if (result.equals("C+") || result.equals("C") || result.equals("C-")) {
            result_cnt[2] = result_cnt[2] + 1;
        } else if (result.equals("D+") || result.equals("D") || result.equals("D-")) {
            result_cnt[3] = result_cnt[3] + 1;
        } else if (result.equals("F")) {
            result_cnt[4] = result_cnt[4] + 1;
        } else {
            result_cnt[5] = result_cnt[5] + 1;
        }
        return result_cnt;
    }

    public Float[] count_1(String result, Float result_cnt_1[]) {

        if (result.equals("F")) {
            result_cnt_1[2] = result_cnt_1[2] + 1;
        } else if (result.equals("D")||result.equals("D-")||result.equals("D+")||result.equals("C-")) {
            result_cnt_1[1] = result_cnt_1[1] + 1;
        } else {
            result_cnt_1[0] = result_cnt_1[0] + 1;
        }
        return result_cnt_1;
    }
}
