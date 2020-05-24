package net.beeapm.agent.common;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.Map;

public class MatchKit {
    public static ElementMatcher.Junction<TypeDescription> buildTypesMatcher(Map<String, String> includeMap, Map<String, String> excludeMap) {
        ElementMatcher.Junction<TypeDescription> matcher = ElementMatchers.not(ElementMatchers.<TypeDescription>nameStartsWith("net.beeapm.agent."));
        if (includeMap != null && !includeMap.isEmpty()) {
            String namedVal = includeMap.get("named");
            String nameStartsWithVal = includeMap.get("nameStartsWith");
            String nameEndsWithVal = includeMap.get("nameEndsWith");
            String nameContainsVal = includeMap.get("nameContains");
            String nameMatchesVal = includeMap.get("nameMatches");
            String hasSuperTypeVal = includeMap.get("hasSuperType");
            String hasAnnotationVal = includeMap.get("hasAnnotation");
            ElementMatcher.Junction<TypeDescription> includeMatcher = null;
            if (BeeUtils.isNotBlank(namedVal)) {
                String[] arr = namedVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.named(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.named(arr[i]));
                }
            }
            if (BeeUtils.isNotBlank(nameStartsWithVal)) {
                String[] arr = nameStartsWithVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.nameStartsWith(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameStartsWith(arr[i]));
                }
            }
            if (BeeUtils.isNotBlank(nameEndsWithVal)) {
                String[] arr = nameEndsWithVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.nameEndsWith(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameEndsWith(arr[i]));
                }
            }
            if (BeeUtils.isNotBlank(nameContainsVal)) {
                String[] arr = nameContainsVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.nameContains(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameContains(arr[i]));
                }
            }
            if (BeeUtils.isNotBlank(nameMatchesVal)) {
                String[] arr = nameMatchesVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.nameMatches(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameMatches(arr[i]));
                }
            }
            if (BeeUtils.isNotBlank(hasSuperTypeVal)) {
                String[] arr = hasSuperTypeVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.hasSuperType(ElementMatchers.<TypeDescription>named(arr[i]));
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.hasSuperType(ElementMatchers.<TypeDescription>named(arr[i])));
                }
            }
            if (BeeUtils.isNotBlank(hasAnnotationVal)) {
                String[] arr = hasAnnotationVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.hasAnnotation(ElementMatchers.annotationType(ElementMatchers.<TypeDescription>named(arr[i])));
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.hasAnnotation(ElementMatchers.annotationType(ElementMatchers.<TypeDescription>named(arr[i]))));
                }
            }
            if (includeMatcher != null) {
                matcher = matcher.and(includeMatcher);
            }

        }
        if (excludeMap != null && !excludeMap.isEmpty()) {
            String namedVal = excludeMap.get("named");
            String nameStartsWithVal = excludeMap.get("nameStartsWith");
            String nameEndsWithVal = excludeMap.get("nameEndsWith");
            String nameContainsVal = excludeMap.get("nameContains");
            String nameMatchesVal = excludeMap.get("nameMatches");
            String hasSuperTypeVal = excludeMap.get("hasSuperType");
            String hasAnnotationVal = excludeMap.get("hasAnnotation");
            ElementMatcher.Junction<TypeDescription> excludeMatch = ElementMatchers.none();
            if (BeeUtils.isNotBlank(namedVal)) {
                String[] arr = namedVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.named(arr[i])));
                }
            }
            if (BeeUtils.isNotBlank(nameStartsWithVal)) {
                String[] arr = nameStartsWithVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameStartsWith(arr[i])));
                }
            }
            if (BeeUtils.isNotBlank(nameEndsWithVal)) {
                String[] arr = nameEndsWithVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameEndsWith(arr[i])));
                }
            }
            if (BeeUtils.isNotBlank(nameContainsVal)) {
                String[] arr = nameContainsVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameContains(arr[i])));
                }
            }
            if (BeeUtils.isNotBlank(nameMatchesVal)) {
                String[] arr = nameMatchesVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameMatches(arr[i])));
                }
            }
            if (BeeUtils.isNotBlank(hasSuperTypeVal)) {
                String[] arr = hasSuperTypeVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.hasSuperType(ElementMatchers.<TypeDescription>named(arr[i]))));
                }
            }
            if (BeeUtils.isNotBlank(hasAnnotationVal)) {
                String[] arr = hasAnnotationVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.hasAnnotation(ElementMatchers.annotationType(ElementMatchers.<TypeDescription>named(arr[i])))));
                }
            }
        }
        return matcher;
    }

    public static ElementMatcher.Junction<MethodDescription> buildMethodsMatcher(Map<String, String> includeMap, Map<String, String> excludeMap) {
        ElementMatcher.Junction<MethodDescription> matcher = ElementMatchers.isMethod();
        if (includeMap != null && !includeMap.isEmpty()) {
            String namedVal = includeMap.get("named");
            String nameStartsWithVal = includeMap.get("nameStartsWith");
            String nameEndsWithVal = includeMap.get("nameEndsWith");
            String nameContainsVal = includeMap.get("nameContains");
            String nameMatchesVal = includeMap.get("nameMatches");
            String isAnnotatedWith = includeMap.get("isAnnotatedWith");
            ElementMatcher.Junction<MethodDescription> includeMatcher = null;
            if (BeeUtils.isNotBlank(namedVal)) {
                String[] arr = namedVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.named(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.named(arr[i]));
                }
            }
            if (BeeUtils.isNotBlank(nameStartsWithVal)) {
                String[] arr = nameStartsWithVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.nameStartsWith(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameStartsWith(arr[i]));
                }
            }
            if (BeeUtils.isNotBlank(nameEndsWithVal)) {
                String[] arr = nameEndsWithVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.nameEndsWith(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameEndsWith(arr[i]));
                }
            }
            if (BeeUtils.isNotBlank(nameContainsVal)) {
                String[] arr = nameContainsVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.nameContains(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameContains(arr[i]));
                }
            }
            if (BeeUtils.isNotBlank(nameMatchesVal)) {
                String[] arr = nameMatchesVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.nameMatches(arr[i]);
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.nameMatches(arr[i]));
                }
            }
            if (BeeUtils.isNotBlank(isAnnotatedWith)) {
                String[] arr = isAnnotatedWith.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (includeMatcher == null) {
                        includeMatcher = ElementMatchers.isAnnotatedWith(ElementMatchers.<TypeDescription>named(arr[i]));
                        continue;
                    }
                    includeMatcher = includeMatcher.or(ElementMatchers.<MethodDescription>isAnnotatedWith(ElementMatchers.<TypeDescription>named(arr[i])));
                }
            }
            if (includeMatcher != null) {
                matcher = matcher.and(includeMatcher);
            }
        }

        if (excludeMap != null && !excludeMap.isEmpty()) {
            String namedVal = excludeMap.get("named");
            String nameStartsWithVal = excludeMap.get("nameStartsWith");
            String nameEndsWithVal = excludeMap.get("nameEndsWith");
            String nameContainsVal = excludeMap.get("nameContains");
            String nameMatchesVal = excludeMap.get("nameMatches");
            String isAnnotatedWith = excludeMap.get("isAnnotatedWith");
            if (BeeUtils.isNotBlank(namedVal)) {
                String[] arr = namedVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.named(arr[i])));
                }
            }
            if (BeeUtils.isNotBlank(nameStartsWithVal)) {
                String[] arr = nameStartsWithVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>nameStartsWith(arr[i])));
                }
            }
            if (BeeUtils.isNotBlank(nameEndsWithVal)) {
                String[] arr = nameEndsWithVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>nameEndsWith(arr[i])));
                }
            }
            if (BeeUtils.isNotBlank(nameContainsVal)) {
                String[] arr = nameContainsVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>nameContains(arr[i])));
                }
            }
            if (BeeUtils.isNotBlank(nameMatchesVal)) {
                String[] arr = nameMatchesVal.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>nameMatches(arr[i])));
                }
            }
            if (BeeUtils.isNotBlank(isAnnotatedWith)) {
                String[] arr = isAnnotatedWith.split(",");
                for (int i = 0; i < arr.length; i++) {
                    matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>isAnnotatedWith(ElementMatchers.<TypeDescription>named(arr[i]))));
                }
            }
        }
        return matcher;
    }
}
