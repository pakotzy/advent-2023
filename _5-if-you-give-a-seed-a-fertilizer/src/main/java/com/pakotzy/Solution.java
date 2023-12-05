package com.pakotzy;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Solution {
    @SuppressWarnings("java:S6204")
    public long findNearestLocation(List<String> almanac) {
        // Needs to be mutable
        List<Long> sources = Arrays.stream(almanac.get(0).substring(7).split(" ")).mapToLong(Long::valueOf).boxed().collect(Collectors.toList());
        return findNearestLocation(sources, almanac.subList(2, almanac.size()));
    }

    public long findNearestLocationInSeedRanges(List<String> almanac) {
        List<Long> sourcesInstructions = Arrays.stream(almanac.get(0).substring(7).split(" ")).mapToLong(Long::valueOf).boxed().toList();
        List<Long> sources = new ArrayList<>();
        int i = 0;
        int instructionsSize = sourcesInstructions.size() - 1;
        while (i < instructionsSize) {
            Long startPoint = sourcesInstructions.get(i);
            Long range = sourcesInstructions.get(i + 1);
            LongStream.range(startPoint, startPoint + range).forEach(sources::add);

            i += 2;
        }
        return findNearestLocation(sources, almanac.subList(2, almanac.size()));
    }

    private long findNearestLocation(List<Long> sources, List<String> maps) {
        List<Long> tmpSources = new ArrayList<>();
        for (String line : maps) {
            if (Character.isDigit(line.charAt(0))) {
                String[] parameters = line.split(" ");
                tmpSources.addAll(mapToSource(sources, Long.parseLong(parameters[0]), Long.parseLong(parameters[1]), Long.parseLong(parameters[2])));
            } else if (Character.isAlphabetic(line.charAt(0))) {
                tmpSources.addAll(sources);
                sources = tmpSources;
                tmpSources = new ArrayList<>();
            }
        }

        return Stream.concat(sources.stream(), tmpSources.stream()).min(Comparator.naturalOrder()).orElse(Long.MIN_VALUE);
    }

    private List<Long> mapToSource(List<Long> sources, long destinationStart, long sourceStart, long range) {
        List<Long> destinations = new ArrayList<>();
        long sourceEnd = sourceStart + range;

        Iterator<Long> sourcesIterator = sources.iterator();
        while (sourcesIterator.hasNext()) {
            Long source = sourcesIterator.next();
            if (sourceStart <= source && source <= sourceEnd) {
                destinations.add((source - sourceStart) + destinationStart);
                sourcesIterator.remove();
            }
        }

        return destinations;
    }

    public long findNearestLocationInSeedRangesSmart(List<String> almanac) {
        List<Source> sources = new ArrayList<>();

        List<Long> sourcesInstructions = Arrays.stream(almanac.get(0).substring(7).split(" ")).mapToLong(Long::valueOf).boxed().toList();
        int instructionsSize = sourcesInstructions.size() - 1;
        int i = 0;
        while (i < instructionsSize) {
            Long startPoint = sourcesInstructions.get(i);
            Long length = sourcesInstructions.get(i + 1);
            sources.add(new Source(startPoint, length));

            i += 2;
        }
        return findNearestLocationSmart(sources, almanac.subList(2, almanac.size()));
    }

    private long findNearestLocationSmart(List<Source> sources, List<String> maps) {
        List<Source> tmpSources = new ArrayList<>();
        for (String line : maps) {
            if (Character.isDigit(line.charAt(0))) {
                String[] parameters = line.split(" ");
                tmpSources.addAll(mapToDestination(sources, Long.parseLong(parameters[0]), new Source(Long.parseLong(parameters[1]), Long.parseLong(parameters[2]))));
            } else if (Character.isAlphabetic(line.charAt(0))) {
                tmpSources.addAll(sources);
                sources = tmpSources;
                tmpSources = new ArrayList<>();
            }
        }

        return Stream.concat(sources.stream(), tmpSources.stream()).mapToLong(Source::start).min().orElse(Long.MIN_VALUE);
    }

    private List<Source> mapToDestination(List<Source> sources, long destinationStart, Source mapping) {
        List<Source> destinations = new ArrayList<>();
        List<Source> partialSources = new ArrayList<>();

        Iterator<Source> sourcesIterator = sources.iterator();
        while (sourcesIterator.hasNext()) {
            Source source = sourcesIterator.next();
            Optional<Source> optionalIntersection = getIntersection(source, mapping);
            if (optionalIntersection.isPresent()) {
                Source intersection = optionalIntersection.get();
                if (intersection.start != source.start) {
                    partialSources.add(new Source(Math.min(intersection.start, source.start), Math.abs(intersection.length - source.length)));
                } else if (intersection.length != source.length) {
                    partialSources.add(new Source(intersection.start + intersection.length, Math.abs(intersection.length - source.length)));
                }

                destinations.add(mapTo(destinationStart, mapping, intersection));
                sourcesIterator.remove();
            }
        }

        sources.addAll(partialSources);
        return destinations;
    }

    private Optional<Source> getIntersection(Source first, Source second) {
        long commonStart = Math.max(first.start, second.start);
        long commonEnd = Math.min(first.start + first.length - 1, second.start + second.length - 1);

        if (commonStart <= commonEnd) {
            return Optional.of(new Source(commonStart, commonEnd - commonStart + 1));
        } else {
            return Optional.empty();
        }
    }

    private Source mapTo(long destinationStart, Source mapping, Source intersection) {
        return new Source(destinationStart + (intersection.start - mapping.start), intersection.length);
    }

    private record Source(long start, long length) { }
}
