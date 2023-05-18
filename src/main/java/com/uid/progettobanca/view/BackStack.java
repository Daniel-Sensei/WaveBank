package com.uid.progettobanca.view;

import javafx.scene.Parent;

import java.io.IOException;
import java.util.Stack;

public class BackStack extends Stack<String> {
    private static BackStack instance;
    private Stack<BackStackItem> stack;

    private BackStack() {
        stack = new Stack<>();
    }

    public static BackStack getInstance() {
        if (instance == null) {
            synchronized (BackStack.class) {
                if (instance == null) {
                    instance = new BackStack();
                }
            }
        }
        return instance;
    }

    public void push(String title, Parent parent) {
        stack.push(new BackStackItem(title, parent));
        super.push(title);
    }

    public String popTitle() {
        if (!stack.isEmpty()) {
            stack.pop();
            return super.pop();
        }
        return null;
    }

    public Parent popParent() {
        if (!stack.isEmpty()) {
            BackStackItem item = stack.pop();
            super.pop();
            return item.getParent();
        }
        return null;
    }

    private class BackStackItem {
        private String title;
        private Parent parent;

        public BackStackItem(String title, Parent parent) {
            this.title = title;
            this.parent = parent;
        }

        public String getTitle() {
            return title;
        }

        public Parent getParent() {
            return parent;
        }
    }

    public void loadPreviousPage() throws IOException {
        if (this.size() > 1) {
            //stampa elementi dello stack
            this.popTitle(); //rimuove pagina attuale
            String title = this.popTitle(); //preleva pagina precedente
            System.out.println("Title: " + title);
            SceneHandler.getInstance().setPage(title);
        }
    }
}