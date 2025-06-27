// UserQuizAnswer.java (Entity)
package com.taletrails.taletrails_backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user_quiz_answers")
public class UserQuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "question_text")
    private String question;

    @Column(name = "selected_option")
    private String selectedOption;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getSelectedOption() { return selectedOption; }
    public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }
}