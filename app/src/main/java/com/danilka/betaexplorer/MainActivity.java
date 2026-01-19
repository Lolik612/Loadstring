package com.danilka.betaexplorer;

import android.os.Bundle;
import android.os.Environment;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    
    private ListView listView;
    private TextView pathView;
    private ArrayAdapter<String> adapter;
    private List<String> items = new ArrayList<>();
    private String currentPath;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Находим элементы
        listView = findViewById(R.id.listView);
        pathView = findViewById(R.id.pathView);
        Button btnHome = findViewById(R.id.btnHome);
        Button btnUp = findViewById(R.id.btnUp);
        
        // Адаптер для списка
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.itemText, items);
        listView.setAdapter(adapter);
        
        // Обработчики кнопок
        btnHome.setOnClickListener(v -> goHome());
        btnUp.setOnClickListener(v -> goUp());
        
        // Клик по элементу
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String item = items.get(position);
            if (item.startsWith("[📁]")) {
                // Это папка
                String folderName = item.substring(4);
                loadFiles(currentPath + "/" + folderName);
            } else {
                // Это файл
                Toast.makeText(this, "Файл: " + item, Toast.LENGTH_SHORT).show();
            }
        });
        
        // Загружаем домашнюю папку
        goHome();
    }
    
    private void goHome() {
        currentPath = Environment.getExternalStorageDirectory().getPath();
        loadFiles(currentPath);
    }
    
    private void goUp() {
        File currentDir = new File(currentPath);
        File parent = currentDir.getParentFile();
        if (parent != null) {
            loadFiles(parent.getPath());
        }
    }
    
    private void loadFiles(String path) {
        items.clear();
        currentPath = path;
        pathView.setText("📂 Путь: " + path);
        
        File dir = new File(path);
        File[] files = dir.listFiles();
        
        if (files != null) {
            // Сначала папки
            for (File file : files) {
                if (file.isDirectory()) {
                    items.add("[📁] " + file.getName());
                }
            }
            // Потом файлы
            for (File file : files) {
                if (!file.isDirectory()) {
                    items.add("[📄] " + file.getName() + " (" + formatSize(file.length()) + ")");
                }
            }
        }
        
        // Если папка пустая
        if (items.isEmpty()) {
            items.add("📭 Папка пустая");
        }
        
        adapter.notifyDataSetChanged();
    }
    
    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " Б";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " КБ";
        return String.format("%.1f МБ", bytes / (1024.0 * 1024));
    }
                  }
