package com.example.dreamgarden.Common;

import com.example.dreamgarden.Models.Category;
import com.example.dreamgarden.Models.Foods;
import com.example.dreamgarden.Models.Gallery;
import com.example.dreamgarden.Models.Images;
import com.example.dreamgarden.Models.Tables;
import com.example.dreamgarden.Models.User;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class Common {
    public static final String USER_REFERENCES = "Users";
    public static final String ORDER_REF = "Orders";
    public static final String BOOK_NOW_REF = "BookNow";
    public static final String TABLES_REF = "Tables";
    public static User currentUser;

    public static String CATEGORY_REF = "Category";
    public static Category categorySelected;
    public static Foods selectedFood;

    public static String GALLERY_REF = "Gallery";
    public static Gallery gallerySelected;
    public static Images selectedImage;

    public static String POPULAR_CATEGORY_REF = "MostPopular";;
    public static String BEST_DEALS_REF = "BestDials";

    public static final int DEFAULT_COLUMN_COUNT = 0;
    public static  final int FULL_WIDTH_COLUMN = 1;

    public static Tables selectedTable;

    public static String formatPrice(double price) {
        if (price != 0) {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            df.setRoundingMode(RoundingMode.UP);
            String finalPrice = new StringBuilder(df.format(price)).toString();
            return finalPrice.replace(".", ",");
        }
        else
            return "0,00";
    }

    public static String createOrderNumber() {
        return new StringBuilder()
                .append(System.currentTimeMillis())
                .append(Math.abs(new Random().nextInt()))
                .toString();
    }

    public static String bookNumber() {
        return new StringBuilder()
                .append(System.currentTimeMillis())
                .append(Math.abs(new Random().nextInt()))
                .toString();
    }
}
