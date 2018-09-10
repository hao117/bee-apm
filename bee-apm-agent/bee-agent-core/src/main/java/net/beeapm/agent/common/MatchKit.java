package net.beeapm.agent.common;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class MatchKit {
    public static ElementMatcher.Junction<TypeDescription> buildTypesMatcher(Map<String,String> includeMap,Map<String,String> excludeMap){
        ElementMatcher.Junction<TypeDescription>  matcher = ElementMatchers.not(ElementMatchers.<TypeDescription>nameStartsWith("net.beeapm.agent."));
        if(includeMap != null && !includeMap.isEmpty()){
            String namedVal = includeMap.get("named");
            String nameStartsWithVal = includeMap.get("nameStartsWith");
            String nameEndsWithVal = includeMap.get("nameEndsWith");
            String nameContainsVal = includeMap.get("nameContains");
            String nameMatchesVal = includeMap.get("nameMatches");
            String hasSuperTypeVal = includeMap.get("hasSuperType");
            String hasAnnotationVal = includeMap.get("hasAnnotation");
            if(StringUtils.isNotBlank(namedVal)){
                matcher = matcher.and(ElementMatchers.named(namedVal));
            }
            if(StringUtils.isNotBlank(nameStartsWithVal)){
                matcher = matcher.and(ElementMatchers.<TypeDescription>nameStartsWith(nameStartsWithVal));
            }
            if(StringUtils.isNotBlank(nameEndsWithVal)){
                matcher = matcher.and(ElementMatchers.<TypeDescription>nameEndsWith(nameEndsWithVal));
            }
            if(StringUtils.isNotBlank(nameContainsVal)){
                matcher = matcher.and(ElementMatchers.<TypeDescription>nameContains(nameContainsVal));
            }
            if(StringUtils.isNotBlank(nameMatchesVal)){
                matcher = matcher.and(ElementMatchers.<TypeDescription>nameMatches(nameMatchesVal));
            }
            if(StringUtils.isNotBlank(hasSuperTypeVal)){
                matcher = matcher.and(ElementMatchers.hasSuperType(ElementMatchers.<TypeDescription>named(hasSuperTypeVal)));
            }
            if(StringUtils.isNotBlank(hasAnnotationVal)){
                matcher = matcher.and(ElementMatchers.hasAnnotation(ElementMatchers.annotationType(ElementMatchers.<TypeDescription>named(hasAnnotationVal))));
            }
        }
        if(excludeMap != null && !excludeMap.isEmpty()) {
            String namedVal = excludeMap.get("named");
            String nameStartsWithVal = excludeMap.get("nameStartsWith");
            String nameEndsWithVal = excludeMap.get("nameEndsWith");
            String nameContainsVal = excludeMap.get("nameContains");
            String nameMatchesVal = excludeMap.get("nameMatches");
            String hasSuperTypeVal = excludeMap.get("hasSuperType");
            String hasAnnotationVal = excludeMap.get("hasAnnotation");
            if (StringUtils.isNotBlank(namedVal)) {
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.named(namedVal)));
            }
            if (StringUtils.isNotBlank(nameStartsWithVal)) {
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameStartsWith(nameStartsWithVal)));
            }
            if (StringUtils.isNotBlank(nameEndsWithVal)) {
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameEndsWith(nameEndsWithVal)));
            }
            if (StringUtils.isNotBlank(nameContainsVal)) {
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameContains(nameContainsVal)));
            }
            if (StringUtils.isNotBlank(nameMatchesVal)) {
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.<TypeDescription>nameMatches(nameMatchesVal)));
            }
            if (StringUtils.isNotBlank(hasSuperTypeVal)) {
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.hasSuperType(ElementMatchers.<TypeDescription>named(hasSuperTypeVal))));
            }
            if (StringUtils.isNotBlank(hasAnnotationVal)) {
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.hasAnnotation(ElementMatchers.annotationType(ElementMatchers.<TypeDescription>named(hasAnnotationVal)))));
            }
        }
        return matcher;
    }
    public static ElementMatcher.Junction<MethodDescription> buildMethodsMatcher(Map<String,String> includeMap,Map<String,String> excludeMap){
        ElementMatcher.Junction<MethodDescription>  matcher = ElementMatchers.isMethod();
        if(includeMap != null && !includeMap.isEmpty()) {
            String namedVal = includeMap.get("named");
            String nameStartsWithVal = includeMap.get("nameStartsWith");
            String nameEndsWithVal = includeMap.get("nameEndsWith");
            String nameContainsVal = includeMap.get("nameContains");
            String nameMatchesVal = includeMap.get("nameMatches");
            String isAnnotatedWith = includeMap.get("isAnnotatedWith");
            if(StringUtils.isNotBlank(namedVal)){
                matcher = matcher.and(ElementMatchers.named(namedVal));
            }
            if(StringUtils.isNotBlank(nameStartsWithVal)){
                matcher = matcher.and(ElementMatchers.<MethodDescription>nameStartsWith(nameStartsWithVal));
            }
            if(StringUtils.isNotBlank(nameEndsWithVal)){
                matcher = matcher.and(ElementMatchers.<MethodDescription>nameEndsWith(nameEndsWithVal));
            }
            if(StringUtils.isNotBlank(nameContainsVal)){
                matcher = matcher.and(ElementMatchers.<MethodDescription>nameContains(nameContainsVal));
            }
            if(StringUtils.isNotBlank(nameMatchesVal)){
                matcher = matcher.and(ElementMatchers.<MethodDescription>nameMatches(nameMatchesVal));
            }
            if(StringUtils.isNotBlank(isAnnotatedWith)){
                matcher = matcher.and(ElementMatchers.<MethodDescription>isAnnotatedWith(ElementMatchers.<TypeDescription>named(isAnnotatedWith)));
            }
        }

        if(excludeMap != null && !excludeMap.isEmpty()) {
            String namedVal = excludeMap.get("named");
            String nameStartsWithVal = excludeMap.get("nameStartsWith");
            String nameEndsWithVal = excludeMap.get("nameEndsWith");
            String nameContainsVal = excludeMap.get("nameContains");
            String nameMatchesVal = excludeMap.get("nameMatches");
            String isAnnotatedWith = excludeMap.get("isAnnotatedWith");
            if(StringUtils.isNotBlank(namedVal)){
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.named(namedVal)));
            }
            if(StringUtils.isNotBlank(nameStartsWithVal)){
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>nameStartsWith(nameStartsWithVal)));
            }
            if(StringUtils.isNotBlank(nameEndsWithVal)){
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>nameEndsWith(nameEndsWithVal)));
            }
            if(StringUtils.isNotBlank(nameContainsVal)){
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>nameContains(nameContainsVal)));
            }
            if(StringUtils.isNotBlank(nameMatchesVal)){
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>nameMatches(nameMatchesVal)));
            }
            if(StringUtils.isNotBlank(isAnnotatedWith)){
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.<MethodDescription>isAnnotatedWith(ElementMatchers.<TypeDescription>named(isAnnotatedWith))));
            }
        }
        return matcher;
    }
}
