package com.uid.progettobanca.view;

import javafx.scene.Parent;

import java.io.IOException;
import java.util.Stack;

/*
    Singleton class used to manage the back button
    it stores the title of the page and the parent
 */
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
        // avoid pushing the same page twice
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

    public void loadPreviousPage() throws IOException {
        if (this.size() > 1) {
            this.popTitle();
            String title = this.popTitle();
            SceneHandler.getInstance().setPage(title);
        }
    }

    public void clearStack() {
        stack.clear();
    }

    // custom class used to store a pair of title and parent
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
}