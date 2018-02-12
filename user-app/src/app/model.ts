
export class Model {
  user;
  items;
  constructor() {
    this.user = "Adam";
    this.items = [new TodoItem("Buy Flowers", false)]
    this.items = [new TodoItem("Get Shoes", false)]
    this.items = [new TodoItem("Collect Tickets", true)]
    this.items = [new TodoItem("Call Joe", false)]
  }
}

export class TodoItem {
  action;
  done;

  constructor(action, done) {
    this.action = action;
    this.done = done;
  }
}
