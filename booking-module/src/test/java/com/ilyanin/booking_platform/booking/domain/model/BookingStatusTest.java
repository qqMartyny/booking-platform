package com.ilyanin.booking_platform.booking.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


public class BookingStatusTest {
    
    @ParameterizedTest
    @MethodSource("allowedTransitionsFromPending")
    void pendingShouldAllowTransitions(BookingStatus target) {
        assertThat(BookingStatus.PENDING.canTransitionTo(target)).isTrue();
    }

    static Stream<BookingStatus> allowedTransitionsFromPending() {
        return Stream.of(
            BookingStatus.APPROVED,
            BookingStatus.REJECTED,
            BookingStatus.CANCELLED
        );
    }

    @ParameterizedTest
    @MethodSource("forbiddenTransitionsFromPending")
    void pendingShouldForbidTransitions(BookingStatus target) {
        assertThat(BookingStatus.PENDING.canTransitionTo(target)).isFalse();
    }

    static Stream<BookingStatus> forbiddenTransitionsFromPending() {
        return Stream.of(
            BookingStatus.PENDING,
            BookingStatus.COMPLETED,
            BookingStatus.CHANGE_REQUESTED
        );
    }

    @ParameterizedTest
    @MethodSource("allowedTransitionsFromApproved")
    void approvedShouldAllowTransitions(BookingStatus target) {
        assertThat(BookingStatus.APPROVED.canTransitionTo(target)).isTrue();
    }

    static Stream<BookingStatus> allowedTransitionsFromApproved() {
        return Stream.of(
            BookingStatus.CHANGE_REQUESTED,
            BookingStatus.COMPLETED,
            BookingStatus.CANCELLED
        );
    }

    @ParameterizedTest
    @MethodSource("forbiddenTransitionsFromApproved")
    void approvedShouldForbidTransitions(BookingStatus target) {
        assertThat(BookingStatus.APPROVED.canTransitionTo(target)).isFalse();
    }

    static Stream<BookingStatus> forbiddenTransitionsFromApproved() {
        return Stream.of(
            BookingStatus.PENDING,
            BookingStatus.APPROVED,
            BookingStatus.REJECTED
        );
    }

    @ParameterizedTest
    @MethodSource("allowedTransitionsFromChangeRequested")
    void changeRequestedShouldAllowTransitions(BookingStatus target) {
        assertThat(BookingStatus.CHANGE_REQUESTED.canTransitionTo(target)).isTrue();
    }

    static Stream<BookingStatus> allowedTransitionsFromChangeRequested() {
        return Stream.of(
            BookingStatus.APPROVED,
            BookingStatus.CANCELLED
        );
    }

    @ParameterizedTest
    @MethodSource("forbiddenTransitionsFromChangeRequested")
    void changeRequestedShouldForbidTransitions(BookingStatus target) {
        assertThat(BookingStatus.CHANGE_REQUESTED.canTransitionTo(target)).isFalse();
    }

    static Stream<BookingStatus> forbiddenTransitionsFromChangeRequested() {
        return Stream.of(
            BookingStatus.PENDING,
            BookingStatus.CHANGE_REQUESTED,
            BookingStatus.REJECTED,
            BookingStatus.COMPLETED
        );
    }

    // @ParameterizedTest
    // @MethodSource("allowedTransitionsFromCancelled")
    // void cancelledShouldAllowTransitions(BookingStatus target) {
    //     assertThat(BookingStatus.CANCELLED.canTransitionTo(target)).isTrue();
    // }

    // static Stream<BookingStatus> allowedTransitionsFromCancelled() {
    //     return Stream.of(

    //     );
    // }

    @ParameterizedTest
    @MethodSource("forbiddenTransitionsFromCancelled")
    void cancelledShouldForbidTransitions(BookingStatus target) {
        assertThat(BookingStatus.CANCELLED.canTransitionTo(target)).isFalse();
    }

    static Stream<BookingStatus> forbiddenTransitionsFromCancelled() {
        return Stream.of(
            BookingStatus.PENDING,
            BookingStatus.APPROVED,
            BookingStatus.REJECTED,
            BookingStatus.CANCELLED,
            BookingStatus.COMPLETED,
            BookingStatus.CHANGE_REQUESTED
        );
    }   

    // @ParameterizedTest
    // @MethodSource("allowedTransitionsFromRejected")
    // void rejectedShouldAllowTransitions(BookingStatus target) {
    //     assertThat(BookingStatus.REJECTED.canTransitionTo(target)).isTrue();
    // }

    // static Stream<BookingStatus> allowedTransitionsFromRejected() {
    //     return Stream.of();
    // }

    @ParameterizedTest
    @MethodSource("forbiddenTransitionsFromRejected")
    void rejectedShouldForbidTransitions(BookingStatus target) {
        assertThat(BookingStatus.REJECTED.canTransitionTo(target)).isFalse();
    }

    static Stream<BookingStatus> forbiddenTransitionsFromRejected() {
        return Stream.of(
            BookingStatus.PENDING,
            BookingStatus.APPROVED,
            BookingStatus.REJECTED,
            BookingStatus.CANCELLED,
            BookingStatus.COMPLETED,
            BookingStatus.CHANGE_REQUESTED
        );
    }  

    // @ParameterizedTest
    // @MethodSource("allowedTransitionsFromCompleted")
    // void completedShouldAllowTransitions(BookingStatus target) {
    //     assertThat(BookingStatus.COMPLETED.canTransitionTo(target)).isTrue();
    // }

    // static Stream<BookingStatus> allowedTransitionsFromCompleted() {
    //     return Stream.of();
    // }

    @ParameterizedTest
    @MethodSource("forbiddenTransitionsFromCompleted")
    void completedShouldForbidTransitions(BookingStatus target) {
        assertThat(BookingStatus.COMPLETED.canTransitionTo(target)).isFalse();
    }

    static Stream<BookingStatus> forbiddenTransitionsFromCompleted() {
        return Stream.of(
            BookingStatus.PENDING,
            BookingStatus.APPROVED,
            BookingStatus.REJECTED,
            BookingStatus.CANCELLED,
            BookingStatus.COMPLETED,
            BookingStatus.CHANGE_REQUESTED
        );
    }  
}
