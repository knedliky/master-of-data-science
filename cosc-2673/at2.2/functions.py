import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np
import pandas as pd
import statsmodels.api as sm
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error
from sklearn.model_selection import train_test_split


def calculate_vif(df, features):
    """
    A function to calculate the variance inflation factors of a dataset
    """
    vif, tolerance = {}, {}

    for feature in features:
        X = [f for f in features if f != feature]
        X, y = df[X], df[feature]

        r2 = LinearRegression().fit(X, y).score(X, y)

        tolerance[feature] = 1 - r2
        vif[feature] = 1 / (tolerance[feature])

    return pd.DataFrame({"vif": vif, "tolerance": tolerance})


def learning_curves(model, X, y, title='Train/ Validation Learning Curves'):
    """
    A function to display the learning curves of a model between testing
    and validation data, to determine overfitting/ underfitting
    """
    fig = plt.figure(figsize=(14, 7))

    X_train, X_val, y_train, y_val = train_test_split(X, y, random_state=27)
    train_errors, val_errors = [], []
    train_size = np.linspace(5, len(X_train), 10, dtype='int')
    
    for m in train_size:
        model.fit(X_train[:m], y_train[:m])
        y_train_hat = model.predict(X_train[:m])
        y_val_hat = model.predict(X_val)
        train_errors.append(mean_squared_error(y_train[:m], y_train_hat, squared=False))
        val_errors.append(mean_squared_error(y_val, y_val_hat, squared=False))

    sns.lineplot(y=train_errors, x=train_size, color='#29AF7FFF', linewidth=3, label='Train')
    sns.lineplot(y=val_errors, x=train_size, color='#482677FF', linewidth=3, label='Validation')

    plt.xlim(0)
    plt.ylim(bottom=0, top=150)
    plt.grid(axis='y', linestyle='--', linewidth=0.5, color='gray')
    plt.title(title, fontsize=20)
    plt.xlabel('Training size')
    plt.ylabel('RMSE')
    plt.show()


def residual_plots(model, X, y):
    '''
    A function to display the residual plots of a model, to determine overfitting/ underfitting
    '''

    fig, ax = plt.subplots(1, 3, figsize=(21,7))

    y_hat = model.predict(X)
    pp_y = sm.ProbPlot(y, fit=True)
    pp_y_hat = sm.ProbPlot(y_hat, fit=True)

    sns.regplot(x=y_hat, y=y, ax=ax[0], line_kws={'color':'r'})
    sns.residplot(x=y_hat, y=y, ax=ax[1], line_kws={'color':'r'})
    pp_y_hat.qqplot(pp_y, line='45', ax=ax[2])

    ax[0].set_title('Actual vs. Predicted', fontsize=16)
    ax[0].set_xlabel('Predicted')
    ax[0].set_ylabel('Actual')
    ax[1].set_title('Residual Plot', fontsize=16)
    ax[1].set_xlabel('Predicted')
    ax[1].set_ylabel('Residuals')
    ax[2].set_title('QQ plot', fontsize=16)
    ax[2].set_xlabel('Predicted Quantiles')
    ax[2].set_ylabel('Actual Quantiles')

    plt.show()


def feature_importance(model, features=None):
    importance = model._final_estimator.coef_
    if features is None:
        features = model[:-1].get_feature_names_out()

    df = pd.DataFrame(zip(features, importance), columns=['feature', 'value'])
    df['abs_value'] = df['value'].apply(lambda x: abs(x))
    df.sort_values(by='abs_value', ascending=False, inplace=True)

    fig = plt.figure(figsize=(7,14))

    sns.barplot(data=df, x='value', y='feature', orient='h')

    plt.title('Feature Importance', fontsize=16)
    plt.show()


def xgb_learning_curves(model, X, y, title='XGBoost Train/ Validation learning curves'):
    X_trans = model[-2].fit_transform(X)

    X_train, X_val, y_train, y_val = train_test_split(X_trans, y)
    eval_set = [(X_train, y_train), (X_val, y_val)]
    model[-1].fit(X_train, y_train, eval_metric='rmse', eval_set=eval_set, verbose=False)

    val_errors = model[-1].evals_result()['validation_1']['rmse']
    train_errors = model[-1].evals_result()['validation_0']['rmse']

    fig = plt.figure(figsize=(14,7))

    sns.lineplot(x=range(len(val_errors)), y=val_errors, color='#482677FF', linewidth=3, label='Validation')
    sns.lineplot(x=range(len(train_errors)), y=train_errors, color='#29AF7FFF', linewidth=3, label='Train')

    plt.title(title, fontsize=20)
    plt.grid(axis='y', linestyle='--', linewidth=0.5, color='gray')
    plt.xlim(0)
    plt.xlabel('Number of trees')
    plt.ylabel('RMSE')
    plt.show()


def gbr_learning_curves(model, X, y, title='Gradient Boosted Train/ Validation learning curves'):
    X_trans = model[-2].fit_transform(X)

    X_train, X_val, y_train, y_val = train_test_split(X_trans, y)
    model[-1].fit(X_train, y_train)

    val_errors = [mean_squared_error(y_val, y_val_hat, squared=False) for y_val_hat in model[-1].staged_predict(X_val)]
    train_errors = [mean_squared_error(y_train, y_train_hat, squared=False) for y_train_hat in model[-1].staged_predict(X_train)]

    fig = plt.figure(figsize=(14,7))

    sns.lineplot(x=range(len(val_errors)), y=val_errors, color='#482677FF', linewidth=3, label='Validation')
    sns.lineplot(x=range(len(train_errors)), y=train_errors, color='#29AF7FFF', linewidth=3, label='Train')

    plt.title(title, fontsize=20)
    plt.grid(axis='y', linestyle='--', linewidth=0.5, color='gray')
    plt.xlim(0)
    plt.xlabel('Number of trees')
    plt.ylabel('RMSE')
    plt.show()