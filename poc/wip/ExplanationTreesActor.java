package grakn.common.poc.wip;
//
//import javax.annotation.Nullable;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///*
/*






//  A1 Answer [friendship(x,z) and hasname(x,n), x = 1, z = 3, n = -1]
//        |
//  { friendship(x,z): Explanation[mutual-friendship-rule, friendship(x,y) -> A2, friendship(y, z) -> A3] ,        hasname(x,n)   : Explanation[has-name,  ... -> A4]}
//                                  /                          \
//  A2 Answer [friendship(x,y), x = 1, y = 2]             A3 Answer[friendship(y, z) -> y = 2, z = 3]





*/
//
//Iterator<Answer> answers = tx.query("...");
//Answer a1 = answers.next()
//
//Answer.Explained explain = a1.toExplained()
//
//Explanation exp = a1.explanation()
//
//
//IdMap  [ Explanation( IdMap, Query, subExplanations(), _hidden_path_key )]
//
//IdMap.toExplain()
//
//
//ConceptMap => Explanation
//
//Explanation
//  |
//  List<Set<Explanation>>
//    |...
//
//
//
// */
//
//
//public class ExplanationTreesActor {
//
//    Map<ExplanationKey, Explanations> explanations;
//
//
//    private class Explanations {
//        Set<ExplanationKey> ownExplanations;
//        @Nullable
//        ExplanationKey siblingExplanation;
//
//        public Explanations() {
//            ownExplanations = new HashSet<>();
//        }
//    }
//
//    public void recordOwnExplanation(ExecutionActor<?> explainer, Request explaining, Map<String, Long> explainedAnswer, Response.Answer explanation) {
//        ExplanationKey explanationKey = new ExplanationKey(explainer, explaining, explainedAnswer);
//        Explanations explanations = this.explanations.computeIfAbsent(explanationKey, key -> new Explanations());
//    }
//
//    public void recordSiblingExplanation(ExecutionActor<?> explainer, Request explaining, Map<String, Long> explainedAnswer, Response.Answer explanation) {
//        ExplanationKey explanationKey = new ExplanationKey(explainer, explaining, explainedAnswer);
//
//
//        ExecutionActor<?> actor = getAnswerSource(explanation);
//        Request request = explanation.sourceRequest();
//        List<Long> partialAnswers = explanation.partialAnswers;
//
//        ExplanationKey siblingKey = new ExplanationKey(explanation.plan())
//
//        Explanations explanations = this.explanations.computeIfAbsent(explanationKey, key -> new Explanations());
//
//    }
//
//    static class ExplanationKey {
//
//        ExecutionActor<?> explainer;
//        Request explaining;
//        Map<String, Long> explainedAnswer;
//
//        public ExplanationKey(final ExecutionActor<?> explainer, final Request explaining, final Map<String, Long> explainedAnswer) {
//            this.explainer = explainer;
//            this.explaining = explaining;
//            this.explainedAnswer = explainedAnswer;
//        }
//    }
//
//
//    static class UserExplanation {
//
//    }
//
//    UserExplanation getRootExplanation(Map<String, Long> userAnswer) {
//        Explanations exp = this.explanations.get(null);
//        List<Set<ExplanationKey>> siblingExplanations = new ArrayList<>();
//        siblingExplanations.add(exp.ownExplanations);
//        while (exp.siblingExplanation != null) {
//            exp = explanations.get(exp.siblingExplanation);
//            siblingExplanations.add(exp.ownExplanations);
//        }
//
//        // wrap list of set of explanations into user-compatible explanation
//
//        return null;
//    }
//}