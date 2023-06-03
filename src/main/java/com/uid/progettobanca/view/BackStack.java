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
        //non effettua il push delle pagine gia presenti nello stack
        if(!stack.isEmpty() && stack.peek().getTitle().equals(title)){
            return;
        }
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
            this.popTitle(); //rimuove pagina attuale
            String title = this.popTitle(); //preleva pagina precedente
            SceneHandler.getInstance().setPage(title);
        }
    }

    public void printStack() {
        System.out.println("STACK:");
        for (int i = 0; i < stack.size(); i++) {
            System.out.println(stack.get(i).getTitle());
        }
    }

    public void clearStack() {
        stack.clear();
    }
}