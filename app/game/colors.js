export const COLORS = {
    BLUE: {
        STRING: 'blue',
        INDEX: 0
    },
    GREEN: {
        STRING: 'green',
        INDEX: 1
    },
    GREY: {
        STRING: 'grey',
        INDEX: 2
    },
    PURPLE: {
        STRING: 'purple',
        INDEX: 3
    },
    RED: {
        STRING: 'red',
        INDEX: 4
    },
    YELLOW: {
        STRING: 'yellow',
        INDEX: 5
    }
};
export function valueOfColor(color) {
    for (let current in COLORS) {
        if(COLORS[current].STRING  === color) {
            return COLORS[current].INDEX;
        }
    }
}
export function colorOfIndex(index) {
    for (let current in COLORS) {
        if(COLORS[current].INDEX  === index) {
            return COLORS[current].STRING;
        }
    }
}